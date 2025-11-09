package com.bipinkish.store.filters;

import com.bipinkish.store.config.JwtConfig;
import com.bipinkish.store.services.Jwt;
import com.bipinkish.store.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Jwt AuthFilter Started");
        var authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            var token = authHeader.replace("Bearer ", "");
            var jwt = jwtService.parse(token);
            if(jwt != null && !jwt.isExpired()){
                var userId = jwt.getUserId();
                var role = jwt.getRole();
                var authentication = new UsernamePasswordAuthenticationToken(
                        userId,null, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }
            filterChain.doFilter(request,response);
            return;
        }
        System.out.println("[JwtAuthFilter] No token found in request — current authentication: "
                + SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);
        System.out.println("[After Chain] Authentication in context for " + request.getRequestURI() + ": "
                + SecurityContextHolder.getContext().getAuthentication());
    }
}
