package com.example.app.services;

import com.example.web.dto.LoginForm;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class LoginService {

    private final Logger logger = Logger.getLogger(String.valueOf(LoginService.class));

    public boolean authenticate(LoginForm loginForm) {
        logger.info("Try auth with user-form: " + loginForm);
        return loginForm.getUsername().equals("root") && loginForm.getPassword().equals("123");
    }
}
