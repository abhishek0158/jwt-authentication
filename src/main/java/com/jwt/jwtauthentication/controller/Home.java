package com.jwt.jwtauthentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @RequestMapping("/welcome")
    public String welcome(){
        String text="This is private page Not allowed for unauthorized users";
        return text;
    }


}
