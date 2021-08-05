package idv.demo.backend.web;

import idv.demo.backend.security.TokenAuthentication;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class AuthFilter
        extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain
    )
            throws
            ServletException,
            IOException {

        checkAuthorizationHeader(request);
        chain.doFilter(request, response);
    }

    protected void checkAuthorizationHeader(HttpServletRequest request) {

        final String token = request.getHeader("Authorization");
        //log.debug("{} {} token {}", request.getMethod(), request.getRequestURI(), token);
        if (!StringUtils.hasText(token)) {
            return;
        }

        TokenAuthentication user = new TokenAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(user);
    }
}