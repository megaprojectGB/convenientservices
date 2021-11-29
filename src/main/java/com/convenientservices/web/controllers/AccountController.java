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

    @PostMapping()
    public String saveEditUser(Model model,
                                   @ModelAttribute("user") User user,
                                   @RequestParam String role,
                                   @RequestParam String matchingPassword
    ) {
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getEmail());
        System.out.println(user.getPhone());
        System.out.println(user.getRoles());
//        String role = "user";
//        String matchingPassword = "pass";
        String answer = userService.registerNewUser(user, role, matchingPassword);
        if ("success".equals(answer)) {
            model.addAttribute("success", true);
            return "account";
        }
        model.addAttribute("answer", answer);
        return "edit-profile";
    }
}
