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
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;

    @GetMapping
    public String showAccountPage(Principal principal,
                                  Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "account";
    }

    @GetMapping("/edit")
    public String saveEditUser(Principal principal, Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute(new User());
        model.addAttribute("answer", "");
        return "edit-profile";
    }

    @PostMapping("/edit")
    public String saveEditUser(Model model,
                               Principal principal,
                               @ModelAttribute("role") Long role,
                               @ModelAttribute("matchingPassword") String matchingPassword,
                               @ModelAttribute("user") User user) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        System.out.println(user.toString());
        System.out.println("role user is " + role);
        System.out.println("matchingPassword is - " + matchingPassword);
        String answer = userService.saveEditUser(principal, user, role, matchingPassword);
        if ("success".equals(answer)) {
            model.addAttribute("success", true);
            return "redirect:/account";
        }
        model.addAttribute("answer", answer);
        return "edit-profile";
    }
}
