package easy.market.security;

import easy.market.repository.RedisTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JWTLogoutFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/logout");
    private final RedisTokenRepository redisTokenRepository;
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("logout request={}", request.getRequestURI());
        // 로그아웃  경로가 아니라면 다음 필터로
        if(!requestMatcher.matches(request)) {
            log.info("request not match");
            filterChain.doFilter(request, response);
            return;
        }
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(SecurityConst.REFRESH_TOKEN))
                .map(cookie -> cookie.getValue())
                .findAny()
                .orElse(null);

        if(!StringUtils.hasText(refreshToken)) {
            return;
        }

        deleteCookie(response);

        log.info("Refresh token: {}", refreshToken);
        Claims payload = null;
        try {
            payload = jwtUtil.getPayload(refreshToken, SecurityConst.REFRESH_TOKEN);
        }
        catch (ExpiredJwtException | SignatureException | MalformedJwtException | IllegalArgumentException e){
            log.error(e.getMessage());;
            return;
        }
        String username = jwtUtil.getUsername(payload);
        log.info("username: {}", username);
        redisTokenRepository.removeRefreshToken(username);
    }
    private void deleteCookie(HttpServletResponse response){
        Cookie cookie = new Cookie(SecurityConst.REFRESH_TOKEN, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
