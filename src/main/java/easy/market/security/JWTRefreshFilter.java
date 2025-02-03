package easy.market.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import easy.market.repository.RedisTokenRepository;
import easy.market.response.ErrorResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JWTRefreshFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/refresh");
    private final RedisTokenRepository redisTokenRepository;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 재발급 경로가 아니라면 다음 필터로
        if(!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getCookies() == null){
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "No refresh token found");
            return;
        }

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(SecurityConst.REFRESH_TOKEN))
                .map(cookie -> cookie.getValue())
                .findAny()
                .orElse(null);

        // 토큰이 존재하지 않으면
        if(!StringUtils.hasText(refreshToken)) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "No refresh token found");
            return;
        }
        Claims payload = null;

        try {
            payload = jwtUtil.getPayload(refreshToken, SecurityConst.REFRESH_TOKEN);
        }
        catch (ExpiredJwtException e){
            log.error(e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "refresh_expired");
            return;
        }
        catch (SignatureException | MalformedJwtException | IllegalArgumentException e){
            log.error(e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "invalid_signature");
            return;
        }

        String username = jwtUtil.getUsername(payload);
        String role = jwtUtil.getRole(payload);
        String findRefreshToken = redisTokenRepository.getRefreshToken(username);

        // redis token 만료
        if(!StringUtils.hasText(findRefreshToken)) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "refresh_expired");
        }

        if(findRefreshToken.equals(refreshToken)) {
            String accessToken = jwtUtil.createJwt(SecurityConst.ACCESS_TOKEN, username, role, SecurityConst.ACCESS_TOKEN_EXPIRED_MS);
            response.setHeader(SecurityConst.ACCESS_TOKEN, accessToken);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        ErrorResponse errorResponse = new ErrorResponse(message);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(jsonResponse);
    }
}
