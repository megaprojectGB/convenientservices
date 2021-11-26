package com.convenientservices.web.controllers;

import com.convenientservices.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserService userService;

    @GetMapping
    public String showIndexPage(Principal principal,
                                Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("activePage", "");
        return "index";
    }

    @GetMapping("/about")
    public String showAboutPage(Principal principal,
                                Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("activePage", "about");
        return "about";
    }
}
