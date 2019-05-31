package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "/registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("user",user);
        if(result.hasErrors()) {
            return "registration";
        }
        else {
            //If user status is instructor select "ADMIN" role
            if(user.getStatus().equalsIgnoreCase("instructor")) {
                System.out.println("Setting admin priveleges");
                userService.saveAdmin(user);
            } else {
                System.out.println("user role is"+user.getStatus());
                userService.saveUser(user);
            }
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/secure")
    public String secure(Model model) {
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", userService.getCurrentUser());
        return "secure";
    }

}
