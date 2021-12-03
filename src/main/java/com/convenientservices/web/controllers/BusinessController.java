package com.convenientservices.web.controllers;

import com.convenientservices.web.services.PointOfServiceServices;
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
@RequestMapping("/business")
public class BusinessController {
    private final UserService userService;
    private final PointOfServiceServices pointOfServiceServices;

    @GetMapping()
    public String showBusinessSettingsPage(Principal principal,
                                           Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("services", userService.getUserDTOByUserName(principal).getMasterServices());
        model.addAttribute("pointofservices", pointOfServiceServices.findAllByUserBoss(principal));
        return "business";
    }

    @GetMapping("/delete/{id}")
    public String deleteUserPos(Principal principal,
                                Model model,
                                @PathVariable Long id) {

        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("services", userService.getUserDTOByUserName(principal).getMasterServices());
        model.addAttribute("pointofservices", pointOfServiceServices.findAllByUserBoss(principal));
        return "business";
    }

    @GetMapping("/edit/{id}")
    public String editUserPos(Principal principal,
                              Model model,
                              @PathVariable Long id) {

        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "edit_business";
    }

    @GetMapping("/new")
    public String newUserPos(Principal principal,
                             Model model) {

        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "new_business";
    }
}
