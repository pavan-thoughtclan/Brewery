package com.tc.brewery.service;


import com.tc.brewery.entity.User;
import com.tc.brewery.entity.UserRole;
import com.tc.brewery.repository.AddressRepository;
import com.tc.brewery.repository.LoginRepository;
import com.tc.brewery.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginRepository loginRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final String TWILIO_ACCOUNT_SID = "ACb5162fb992d7246e2904ae9889f6689c";
    private final String TWILIO_AUTH_TOKEN = "723389d02270506284b16f1112ff9e54";

    public User findByUsername(String username) {
        return loginRepository.findByEmail(username); // Assuming email is used as the username
    }

    public User findByPhoneNumber(String phoneNumber) {
        return loginRepository.findByPhoneNumber(phoneNumber);
    }

    public User saveuser(User user) {
        User userToSave = new User(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                passwordEncoder.encode(user.getPassword()),
                UserRole.ROLE_USER // Set the default role here
        );
        return loginRepository.save(userToSave);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;

        String actualUsername = null;
        String actualOtp = null;
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "";

//        if (username.matches("^\\+91\\d+$")) {
//            // If the username is a valid phone number (contains only digits)
////            String phoneNumberWithPrefix = "+91" + username;
//            user = loginRepository.findByPhoneNumber(phoneNumberWithPrefix);
//            System.out.println("hello " +user);
//            actualUsername = user.getPhoneNumber();
//            System.out.println("actualUsername"+actualUsername);
//            actualOtp = user.getOtp(); // Get OTP for OTP-based authentication
//        } else if (username.contains("@")) {
//            user = loginRepository.findByEmail(username);
//            actualUsername = user != null ? user.getEmail() : "";
//        } else {
//            httpStatus = HttpStatus.BAD_REQUEST;
//            message = "Invalid username format";
//        }

        if (username.contains("@")) {
            user = loginRepository.findByEmail(username);
            actualUsername = user != null ? user.getEmail() : "";
        }
        else {
            // If the username is a phone number
            if (!username.startsWith("+91")) {
                username = "+91" + username;
            }

            if (username.matches("^\\+91\\d+$")) {
                // If the username is a valid phone number (starts with +91 and followed by digits)
                user = loginRepository.findByPhoneNumber(username);
                actualUsername = user.getPhoneNumber();
//                System.out.println("actualUsername" + actualUsername);
                actualOtp = user.getOtp();

                // Rest of your authentication logic...
            } else {
                httpStatus = HttpStatus.BAD_REQUEST;
                message = "Invalid username format";
            }
        }

        if (user == null) {
            httpStatus = HttpStatus.NOT_FOUND;
            message = "User not found with username: " + actualUsername;
        }

//        if (httpStatus != HttpStatus.OK) {
//            // Return the response with the desired HTTP status and message
//            String responseBody = "{\"message\":\"" + message + "\"}";
//            return (UserDetails) new ResponseEntity<>(responseBody, httpStatus);
//        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));


        if (actualOtp != null) {
            // If actualOtp is not null, return User with actualOtp
            return new org.springframework.security.core.userdetails.User(
                    actualUsername,
                    actualOtp,
                    authorities
            );
        } else {
            // If actualOtp is null, return User with user.getPassword()
            return new org.springframework.security.core.userdetails.User(
                    actualUsername,
                    user.getPassword(),
                    authorities
            );
        }
    }




    public boolean loginviaotpstep1(String username, HttpSession session, RedirectAttributes redirectAttributes) {
        User user1 = null;
        HttpStatus httpStatus = HttpStatus.OK;
        String formattedPhoneNumber=null;
        if (username.matches("^\\d+$")) {
            // If the username is a valid phone number (contains only digits)
            formattedPhoneNumber=("+91" + username);
            user1 = loginRepository.findByPhoneNumber(formattedPhoneNumber);
        } else if (username.contains("@")) {
            user1 = loginRepository.findByEmail(username);
            formattedPhoneNumber =user1.getPhoneNumber();
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        if (user1 == null) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        if (httpStatus != HttpStatus.OK) {
            // Return the response with the desired HTTP status and message
            return false;
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user1.getRole().toString()));

        // Generate OTP (6-digit code)
        String lgotp = generateOtp();
        logger.info("Generated OTP: " + lgotp);

        // Create a UserRegistrationDto instance and set phoneNumber
//        User user1 = new User();
//        user1.setPhoneNumber(phoneNumber);

        // Send OTP using Twilio
        sendOtpViaTwilio(formattedPhoneNumber, lgotp);

        // Set the entered phone number in the session
        session.setAttribute("lenteredphoneno", formattedPhoneNumber);
        session.setAttribute("lgotp", lgotp);
//        user1.setOtp(lgotp); // Set the OTP value in the User entity
//        userRepository.save(user1); // Save the updated User entity
        // Mark the phone number as valid and OTP sent
        // You might want to use a more sophisticated approach to track this state
        // For example, you could store it in the session or database
        return true;
    }

    public boolean loginviaotpstep2(String leotp, HttpServletRequest request) {

        String storedOtp = (String) request.getSession().getAttribute("lgotp");
        logger.info("loginviaotpstep2 method");
        logger.info("Received OTP: {}", leotp);
        logger.info("Stored OTP: {}", storedOtp);
        return storedOtp != null && storedOtp.equals(leotp);
    }


    public boolean sendOtp(String username, HttpSession session, RedirectAttributes redirectAttributes) {
        User user1 = null;
        HttpStatus httpStatus = HttpStatus.OK;
        String formattedPhoneNumber=null;
        if (username.matches("^\\d+$")) {
            // If the username is a valid phone number (contains only digits)
            formattedPhoneNumber=("+91" + username);
            user1 = loginRepository.findByPhoneNumber(formattedPhoneNumber);
        } else if (username.contains("@")) {
            user1 = loginRepository.findByEmail(username);
            formattedPhoneNumber =user1.getPhoneNumber();
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        if (user1 == null) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        if (httpStatus != HttpStatus.OK) {
            // Return the response with the desired HTTP status and message
            return false;
        }

        // Generate OTP (6-digit code)
        String gotp = generateOtp();
        logger.info("Generated OTP: " + gotp);

        // Create a UserRegistrationDto instance and set phoneNumber
//        User user1 = new User();
//        user1.setPhoneNumber(phoneNumber);

        // Send OTP using Twilio
        sendOtpViaTwilio(formattedPhoneNumber, gotp);

        // Set the entered phone number in the session
        session.setAttribute("enteredphoneno", formattedPhoneNumber);
        session.setAttribute("gotp", gotp);

        // Mark the phone number as valid and OTP sent
        // You might want to use a more sophisticated approach to track this state
        // For example, you could store it in the session or database
        return true;
    }

    public boolean verifyOtpAndShowPasswordFields(String eotp, HttpServletRequest request) {

        String storedOtp = (String) request.getSession().getAttribute("gotp");

        logger.info("Inside verifyOtpAndShowPasswordFields method");
        logger.info("Received OTP: {}", eotp);
        logger.info("Stored OTP: {}", storedOtp);
        return storedOtp != null && storedOtp.equals(eotp);
    }


    @Transactional
    public boolean setNewPassword(String newPassword, String confirmNewPassword, HttpServletRequest request) // Add this parameter)
    {
        String enteredphoneno = (String) request.getSession().getAttribute("enteredphoneno");
        User user1= findByPhoneNumber(enteredphoneno);
        logger.info(enteredphoneno);
        logger.info("here phone user 1 "+user1);

        if (user1 == null) {
            return false;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            return false;
        }
        // Encode the password
        String encodedPassword = passwordEncoder.encode(newPassword);
        user1.setPassword(encodedPassword);

        try {
            loginRepository.save(user1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    private String generateOtp() {
        // Generate a 6-digit OTP code
        int otpValue = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otpValue);
    }

    private void sendOtpViaTwilio(String phoneNumber, String otp) {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

        Message message = Message.creator(
                        new PhoneNumber(phoneNumber),
                        new PhoneNumber("+15736484549"),  // Replace with your Twilio phone number
                        "Your OTP is: " + otp)
                .create();
        logger.info("OTP sent: " + message.getSid());
    }
}
