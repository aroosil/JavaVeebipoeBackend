package ee.andu.server.service;

import ee.andu.server.entity.Person;
import ee.andu.server.entity.PersonRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            Person person = jwtService.validateToken(token);

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (person.getRole() == PersonRole.ADMIN) {
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
            }
            if (person.getRole() == PersonRole.SUPERADMIN) {
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("SUPERADMIN"));
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(person.getId(), person.getEmail(), authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication); // see rida laseb kaitstud API otspunktidesse
        }

        filterChain.doFilter(request,response);
    }
}
