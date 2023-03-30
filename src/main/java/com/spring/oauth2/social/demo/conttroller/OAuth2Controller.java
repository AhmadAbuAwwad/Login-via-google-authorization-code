package com.spring.oauth2.social.demo.conttroller;


import com.spring.oauth2.social.demo.models.User;
import com.spring.oauth2.social.demo.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OAuth2Controller {

    @Autowired
    LoginService loginService;

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<String> googleLogin(String code) throws IOException {
        return ResponseEntity.ok(loginService.loginViaGoogle(code));
    }
}
