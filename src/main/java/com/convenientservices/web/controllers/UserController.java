package com.convenientservices.web.controllers;

import com.convenientservices.web.enums.UserActivationState;
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

    private final String EXPIRED_MESSAGE = "Срок действия вашего кода истёк!";
    private final String WRONG_CODE = "Неправильный код активации!";

    @GetMapping("/activate/{code}")
    public String activateUser(Model model, Principal principal,  @PathVariable("code") String activateCode) {
        UserActivationState activationState = userService.activateUser(activateCode);
        if (activationState == UserActivationState.EXPIRED) {
            model.addAttribute("message", EXPIRED_MESSAGE);
            return "activation-error";
        }
        if (activationState == UserActivationState.INVALID_CODE) {
            model.addAttribute("message", WRONG_CODE);
            return "activation-error";
        }

        return "redirect:/main?active=".concat(activationState.toString());
    }

}
