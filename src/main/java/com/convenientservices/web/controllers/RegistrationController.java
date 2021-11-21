package com.convenientservices.web.controllers;

import com.convenientservices.web.entities.User;
import com.convenientservices.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    @GetMapping()
    public String showRegistrationPage(Model model) {
        model.addAttribute(new User());
        model.addAttribute("answer", "");
        return "registration";
    }

    @PostMapping()
    public String registrationUser(Model model,
                                   @ModelAttribute("user") User user,
                                   @RequestParam String role,
                                   @RequestParam String matchingPassword) {
        String answer = userService.registerNewUser(user, role, matchingPassword);
        if ("success".equals(answer)) {
            model.addAttribute("success", true);
            return "login";
        }
        model.addAttribute("answer", answer);
        return "registration";
    }
}
