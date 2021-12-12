package com.convenientservices.web.controllers;

import com.convenientservices.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/activate/{code}")
    public String activateUser(Model model, Principal principal,  @PathVariable("code") String activateCode) {
        boolean activated = userService.activateUser(activateCode);
        return "redirect:/main?active=".concat(String.valueOf(activated));
    }

}
