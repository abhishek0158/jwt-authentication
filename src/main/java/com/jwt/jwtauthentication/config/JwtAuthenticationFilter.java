package com.jwt.jwtauthentication.config;

import com.jwt.jwtauthentication.helper.JwtUtil;
import com.jwt.jwtauthentication.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokeHeader=request.getHeader("Authorization");
        String username=null;
        String jwtToken=null;
        if(requestTokeHeader!=null&&requestTokeHeader.startsWith("Bearer ")){
            jwtToken=requestTokeHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
            if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else {
                System.out.println("Token is not valid");
            }
        }
        filterChain.doFilter(request,response);
    }
}
