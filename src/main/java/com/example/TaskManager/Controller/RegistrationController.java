package com.example.TaskManager.Controller;

import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Repository.AppUserRepository;
import com.example.TaskManager.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private AppUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping(value = "/req/signup", consumes = "application/json")
    public AppUser createUser(@RequestBody AppUser appUser){
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }
}
