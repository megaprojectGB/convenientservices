package com.convenientservices.web.controllers;

import com.convenientservices.web.entities.User;
import com.convenientservices.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class MainController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/main")
    public String showMainPage() {
        return "main";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute(new User());
        return "registration";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/registration")
    public String registrationUser(@ModelAttribute("user") User user,
                                   @RequestParam String role,
                                   @RequestParam String matchingPassword) {
        userService.registerNewUser(user, role, matchingPassword);
        return "redirect:/main";
    }
}
