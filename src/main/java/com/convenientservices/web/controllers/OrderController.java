package com.convenientservices.web.controllers;

import com.convenientservices.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showOrderPage(Principal principal,
                                Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "orders";
    }
}
