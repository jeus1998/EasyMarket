package easy.market.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/check");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
          String accessToken = request.getHeader("access");
          log.info("Access token: {} url: {} ", accessToken, request.getRequestURI());
          if(!StringUtils.hasText(accessToken)) {
              filterChain.doFilter(request, response);
              return;
          }
          Claims payload = null;
          try {
              payload = jwtUtil.getPayload(accessToken, SecurityConst.ACCESS_TOKEN);
          }
          catch (ExpiredJwtException e){
              log.error(e.getMessage());
              sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "access_expired");
              return;
          }
          catch (SignatureException | MalformedJwtException | IllegalArgumentException e){
              log.error(e.getMessage());
              sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "invalid_signature");
              return;
          }

          String role = jwtUtil.getRole(payload);
          String username = jwtUtil.getUsername(payload);

          if(requestMatcher.matches(request)) {
              log.info("checking username: {} url: {}", username, request.getRequestURI());
              sendCheckResponse(response, username);
              return;
          }

          CustomUserDetails customUserDetails = new CustomUserDetails(username, role);
          UsernamePasswordAuthenticationToken authToken =
                  UsernamePasswordAuthenticationToken.authenticated(
                        customUserDetails,
                        null,
                        customUserDetails.getAuthorities());

        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

        SecurityContextHolder.getContextHolderStrategy().clearContext();
    }
    private void sendCheckResponse(HttpServletResponse response, String username) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        String jsonResponse = objectMapper.writeValueAsString(userInfo);
        response.getWriter().write(jsonResponse);
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
