package pe.edu.upeu.AutoAndes.config;

import pe.edu.upeu.AutoAndes.security.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.JwtException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UsuarioDetailsService usuarios;
    private final RestAuthenticationEntryPoint entryPoint;

    protected boolean shouldNotFilter(HttpServletRequest r) {
        String p = r.getServletPath();
        return r.getMethod().equals("OPTIONS") || p.equals("/api/v1/auth/login") || p.startsWith("/swagger-ui") || p.startsWith("/v3/api-docs");
    }

    protected void doFilterInternal(HttpServletRequest r, HttpServletResponse s, FilterChain chain) throws ServletException, IOException {
        String header = r.getHeader("Authorization");
        if (header != null) {
            try {
                if (!header.startsWith("Bearer ")) throw new BadCredentialsException("Token inválido");
                var user = usuarios.loadUserByUsername(jwt.username(header.substring(7)));
                if (!user.isEnabled()) throw new BadCredentialsException("Token inválido");
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            } catch (JwtException | IllegalArgumentException | AuthenticationException ex) {
                SecurityContextHolder.clearContext();
                entryPoint.commence(r, s, new BadCredentialsException("Token inválido"));
                return;
            }
        }
        chain.doFilter(r, s);
    }
}
