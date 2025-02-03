package easy.market.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import easy.market.repository.RedisTokenRepository;
import easy.market.request.LoginRequest;
import easy.market.response.ErrorResponse;
import io.jsonwebtoken.Jwts;
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
        log.info("Login request: {}", loginRequest);
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
        log.info("login success");
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String role = userDetails.getRole();
        String username = userDetails.getUsername();

        // 토큰 생성
        String accessToken = jwtUtil.createJwt(JWTUtil.ACCESS_TOKEN, username, role, 600000L);
        String refreshToken = jwtUtil.createJwt(JWTUtil.REFRESH_TOKEN, username, role, 86400000L);

        // save RefreshToken(redis)
        redisTokenRepository.saveRefreshToken(username, refreshToken);

        // 응답 설정
        response.setHeader(JWTUtil.ACCESS_TOKEN, accessToken);
        response.addCookie(createCookie(JWTUtil.REFRESH_TOKEN, refreshToken));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("login fail");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse errorResponse = new ErrorResponse(failed.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(jsonResponse);
    }
}
