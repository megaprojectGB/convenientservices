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
    public String saveEditUser(Principal principal, Model model
//                               @ModelAttribute("user") User user,
//                               @RequestParam String role,
//                               @RequestParam String matchingPassword
    ) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
//        model.addAttribute("role", user.getRoles());
//        model.addAttribute("role", user.getRoles());

        return "edit-profile";
    }
}
