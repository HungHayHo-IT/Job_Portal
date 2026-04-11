package com.example.BE.security.filter;

import com.example.BE.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Qualifier("publicPaths")
    private final List<String> publicPaths;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(ApplicationConstants.JWT_HEADER);
        if(authHeader!=null){
            try {
                String jwt = authHeader.substring(7);// xóa 'Bearer'
                Environment env = getEnvironment();
                if(env!=null){
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); //tạo một SecretKey từ chuỗi secret (đã lấy từ cấu hình) để sử dụng cho việc ký và xác thực JWT với thuật toán HMAC-SHA256 (HS256
                    if (null != secretKey) {
                        Claims claims = Jwts.parser().verifyWith(secretKey) // thực hiện xác thực và giải mã một JWT để lấy ra các claims (thông tin bên trong).
                                .build().parseSignedClaims(jwt).getPayload();
                        String username = String.valueOf(claims.get("email"));
                        String roles = String.valueOf(claims.get("roles"));
                        Authentication authentication = new UsernamePasswordAuthenticationToken(username,
                                null, AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

            }catch (ExpiredJwtException exception){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token Expired");
                return;
            }catch (Exception exception){
                throw new BadCredentialsException("Invalid token received");
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {  //quyết định có áp dụng filter cho request hiện tại hay không
        String path = request.getRequestURI();
        return publicPaths.stream().anyMatch(publicPath ->
                pathMatcher.match(publicPath, path));
    }


}
