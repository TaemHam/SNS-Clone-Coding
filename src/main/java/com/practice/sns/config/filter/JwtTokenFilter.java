package com.practice.sns.config.filter;

import com.practice.sns.dto.UserDto;
import com.practice.sns.service.UserService;
import com.practice.sns.util.JwtTokenUtils;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/alarm/subscribe");

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        final String token;

        try {
            if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
                log.info("Request with {} check the query param", request.getRequestURI());
                token = request.getQueryString().split("=")[1].trim();
            } else {
                // 헤더 꺼내기
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (header == null || !header.startsWith("Bearer ")) {
                    log.error("Error occurs while getting header. Header is null or invalid");
                    chain.doFilter(request, response);
                    return;
                }
                token = header.split(" ")[1].trim();
            }

            if (JwtTokenUtils.isExpired(token, secretKey)) {
                log.error("Key is expired");
                chain.doFilter(request, response);
                return;
            }

            String userName = JwtTokenUtils.getUserName(token, secretKey);
            UserDto user = userService.loadUserByUserName(userName);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }
}
