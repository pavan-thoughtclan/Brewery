package com.tc.brewery.controller;

import com.tc.brewery.Jwt.JwtHelper;
import com.tc.brewery.entity.User;
import com.tc.brewery.service.LoginService;
import com.tc.brewery.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AuthController {

    private final LoginService loginService;

    @Autowired
    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/userpage")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> home(Model model, Principal principal) {
//        System.out.println("userpage hit");
        String loggedInUsername = principal.getName();
        User user = loginService.findByUsername(loggedInUsername);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"hello user\"}");
    }

    @GetMapping("/adminpage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> homee(Model model, Principal principal) {
        String loggedInUsername = principal.getName();
        User user = loginService.findByUsername(loggedInUsername);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"hello admin\"}");
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(Model model) {
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"welcome\"}");
    }

    @PostMapping("/testing")
    public ResponseEntity<String> testing(@RequestBody String requestJson) {
        System.out.println("Received Request JSON: " + requestJson);

        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(requestJson);
        System.out.println("Response Status Code: " + responseEntity.getStatusCodeValue());

        return responseEntity;
    }
}

