package com.convenientservices.web.controllers;

import com.convenientservices.web.entities.User;
import com.convenientservices.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class MainController {
    private final UserService userService;

    @GetMapping
    public String showIndexPage(Principal principal,
                                Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        return "index";
    }

    @GetMapping("/main")
    public String showMainPage(Principal principal,
                               Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "main";
    }

    @GetMapping("/about")
    public String showAboutPage(Principal principal,
                                Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        return "about";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute(new User());
        model.addAttribute("answer", "");
        return "registration";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/registration")
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
