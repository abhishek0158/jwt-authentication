package com.jwt.jwtauthentication.controller;

import com.jwt.jwtauthentication.helper.JwtUtil;
import com.jwt.jwtauthentication.model.JwtRequest;
import com.jwt.jwtauthentication.model.JwtResponse;
import com.jwt.jwtauthentication.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){
        System.out.println("Hello");
        System.out.println("jwtRequest = " + jwtRequest);
        try{
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    jwtRequest.getUsername(),jwtRequest.getPassword()
            ));
        }
        catch (UsernameNotFoundException e){
            throw  new UsernameNotFoundException("Not found username");
        }
        catch (Exception e){
            System.out.println("This is the issue "+e);
        }
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token=this.jwtUtil.generateToken(userDetails);
        System.out.println("token = " + token);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
