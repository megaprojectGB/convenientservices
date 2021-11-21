package com.convenientservices.web.controllers;

import com.convenientservices.web.services.CategoryService;
import com.convenientservices.web.services.CityService;
import com.convenientservices.web.services.PointOfServiceServices;
import com.convenientservices.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CityService cityService;
    private final PointOfServiceServices pointOfServiceServices;

    @GetMapping()
    public String showMainPage(Principal principal,
                               Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("companies", pointOfServiceServices.findAll());
        model.addAttribute("favourites", userService.getFavourites(principal));
        return "main";
    }
}
