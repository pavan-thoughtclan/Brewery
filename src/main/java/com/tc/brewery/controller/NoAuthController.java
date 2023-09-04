package com.tc.brewery.controller;

import com.tc.brewery.Jwt.JwtHelper;
import com.tc.brewery.entity.JwtRequest;
import com.tc.brewery.entity.JwtResponse;
import com.tc.brewery.entity.User;
import com.tc.brewery.repository.LoginRepository;
import com.tc.brewery.repository.UserRepository;
import com.tc.brewery.service.LoginService;
import com.tc.brewery.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class NoAuthController {

    private final LoginService loginService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public NoAuthController(LoginService loginService, PasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login/passcode-login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        System.out.println(username);
        System.out.println(password);

        if (username.contains("@")) {
            return doAuthenticateByEmail(username, password);
        } else if (username.matches("^\\d+$")) {
            return doAuthenticateByPhoneNumber(username, password);
        } else {
            System.out.println("Invalid input format");
        }
        return null;
    }

    private ResponseEntity<JwtResponse> doAuthenticateByEmail(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            User user = loginService.findByUsername(email);
            String token = this.helper.generateToken(userDetails);
            JwtResponse response = JwtResponse.builder()
                    .jwrToken(token)
                    .userId(user.getId())
                    .username(userDetails.getUsername()).build();
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
    }

    private ResponseEntity<JwtResponse> doAuthenticateByPhoneNumber(String phoneNumber, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(phoneNumber, password);
        try {
            manager.authenticate(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
            User user = loginService.findByPhoneNumber("+91" + phoneNumber);
            String token = this.helper.generateToken(userDetails);
            JwtResponse response = JwtResponse.builder()
                    .jwrToken(token)
                    .userId(user.getId())
                    .username(userDetails.getUsername()).build();
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
    }

    @PostMapping("/auth/login/otp-login1")
    public ResponseEntity<String> loginviaotpstep1(@RequestBody Map<String, String> requestBody, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = requestBody.get("username");
        boolean isPhoneNumberValid = loginService.loginviaotpstep1(username, session, redirectAttributes);
        if (isPhoneNumberValid) {
            return ResponseEntity.ok("{\"message\":\"OTP sent successfully\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid phone number\"}");
        }
    }

    @PostMapping("/auth/login/otp-login2")
    public ResponseEntity<JwtResponse> loginviaotpstep2(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        String leotp = requestBody.get("leotp");
        boolean isOtpValid = loginService.loginviaotpstep2(leotp, request);

        if (isOtpValid) {
            return doAuthenticateForOtpLogin(leotp,request);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


    private ResponseEntity<JwtResponse> doAuthenticateForOtpLogin(String leotp,HttpServletRequest request) {
        User user1 = null;
        String lenteredphoneno = (String) request.getSession().getAttribute("lenteredphoneno");
        user1 = loginRepository.findByPhoneNumber(lenteredphoneno);
        System.out.println(user1);
        String storedOtprr = null;
        user1.setOtp(storedOtprr);
        loginRepository.save(user1);
        user1 = loginRepository.findByPhoneNumber(lenteredphoneno);
        String storedOtp = (String) request.getSession().getAttribute("lgotp");
        String encodedOtp = passwordEncoder.encode(storedOtp);
        user1.setOtp(encodedOtp);
        loginRepository.save(user1);
        lenteredphoneno = lenteredphoneno.replaceAll("\\+91", "");
        System.out.println(lenteredphoneno);
        System.out.println(leotp);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lenteredphoneno, leotp);
        try {
            manager.authenticate(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(lenteredphoneno);
            User user = loginService.findByPhoneNumber("+91" + lenteredphoneno);
            String token = this.helper.generateToken(userDetails);
            JwtResponse response = JwtResponse.builder()
                    .jwrToken(token)
                    .userId(user.getId())
                    .username(userDetails.getUsername()).build();
            String storedOtpr = null;
            user1.setOtp(storedOtpr);
            loginRepository.save(user1);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> requestBody, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = requestBody.get("username");
        boolean isPhoneNumberValid = loginService.sendOtp(username, session,redirectAttributes);

        if (isPhoneNumberValid) {
            return ResponseEntity.ok("{\"message\":\"OTP sent successfully\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid phone number\"}");
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtpAndShowPasswordFields(@RequestBody Map<String, String> requestBody,
                                                                 HttpServletRequest request
    ) {
        String eotp = requestBody.get("eotp");
        boolean isOtpValid = loginService.verifyOtpAndShowPasswordFields(eotp, request);
        if (isOtpValid) {
            return ResponseEntity.ok("{\"message\":\"OTP verified successfully\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Invalid OTP\"}");
        }
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<String> setNewPassword(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request
    ) {
        String newPassword = requestBody.get("newPassword");
        String confirmNewPassword = requestBody.get("confirmNewPassword");
        boolean passwordUpdated = loginService.setNewPassword(newPassword, confirmNewPassword, request);

        if (passwordUpdated) {
            return ResponseEntity.ok("{\"message\":\"Password updated successfully\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Password update failed\"}");
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registerUserAccount(@RequestBody User user) {
        loginService.saveuser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"User registered successfully\"}");
    }
}

