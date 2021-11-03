package com.convenientservices.web.controllers;

import com.convenientservices.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable("code") String activateCode) {
        boolean activated = userService.activateUser(activateCode);
        model.addAttribute("activated", activated);
        return "activate-user";
    }

}