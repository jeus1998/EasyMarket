package easy.market.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import easy.market.repository.RedisTokenRepository;
import easy.market.request.LoginRequest;
import easy.market.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;
    private final RedisTokenRepository redisTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest = getBody(request);
        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());

        return authenticationManager.authenticate(authToken);
    }

    private LoginRequest getBody(HttpServletRequest request){
        try {
            ServletInputStream inputStream = request.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            return objectMapper.readValue(body, LoginRequest.class);
        }
        catch (IOException e){
            throw new AuthenticationServiceException("서버 에러", e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String role = userDetails.getRole();
        String username = userDetails.getUsername();

        // 토큰 생성
        String accessToken = jwtUtil.createJwt(SecurityConst.ACCESS_TOKEN, username, role, SecurityConst.ACCESS_TOKEN_EXPIRED_MS);
        String refreshToken = jwtUtil.createJwt(SecurityConst.REFRESH_TOKEN, username, role, SecurityConst.REFRESH_TOKEN_EXPIRED_MS);

        // save RefreshToken(redis)
        redisTokenRepository.saveRefreshToken(username, refreshToken);

        // 응답 설정
        response.setHeader(SecurityConst.ACCESS_TOKEN, accessToken);
        response.addCookie(createCookie(SecurityConst.REFRESH_TOKEN, refreshToken));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(SecurityConst.REFRESH_TOKEN_EXPIRED_S);
        // cookie.setSecure(true);
        // cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse errorResponse = new ErrorResponse(failed.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(jsonResponse);
    }
}
