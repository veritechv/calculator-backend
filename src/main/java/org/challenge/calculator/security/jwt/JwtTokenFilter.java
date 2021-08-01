package org.challenge.calculator.security.jwt;

import org.challenge.calculator.security.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = getToken(request);
            String email = jwtProvider.getEmailFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

        }catch(Exception e){
            LOGGER.error("Couldn't get jwt token. Failed once per request filter. Continuing filter chain.");
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        String authReq = request.getHeader(JwtProvider.AUTH_HEADER);
        if(authReq!=null && authReq.startsWith(JwtProvider.TOKEN_PREFIX)){
            return authReq.replace(JwtProvider.TOKEN_PREFIX, "").trim();
        }
        return null;
    }
}
