package com.practice.sns.config.filter;

import com.practice.sns.dto.UserDto;
import com.practice.sns.service.UserService;
import com.practice.sns.util.JwtTokenUtils;
import java.io.IOException;
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

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        try {
            // 헤더 꺼내기
            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
                log.error("Error occurs while getting header. Header is null or invalid");
                chain.doFilter(request, response);
                return;
            }
            final String token = header.split(" ")[1].trim();
            UserDto userDetails = userService.loadUserByUserName(JwtTokenUtils.getUserName(token, secretKey));

            if (JwtTokenUtils.isExpired(token, secretKey)) {
                log.error("Key is expired");
                chain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
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
