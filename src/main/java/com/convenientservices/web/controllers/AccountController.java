package com.convenientservices.web.controllers;

import com.convenientservices.web.dto.UserDTO;
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
        model.addAttribute("answer", "");
        return "edit-profile";
    }

    @PostMapping("/edit")
    public String saveEditUser(Model model,
                               Principal principal,
                               @ModelAttribute("matchingPassword") String matchingPassword,
                               @ModelAttribute("password") String password,
                               @ModelAttribute("userDTO") UserDTO user) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        String answer = userService.saveEditUser(principal, user, password, matchingPassword);
        if ("success".equals(answer)) {
            return "redirect:/account";
        }
        model.addAttribute("answer", answer);
        return "edit-profile";
    }
}
