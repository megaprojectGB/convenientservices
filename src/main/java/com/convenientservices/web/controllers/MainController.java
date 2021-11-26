package com.convenientservices.web.controllers;

import com.convenientservices.web.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String showMainPage (Principal principal,
                                Model model, @Param(value = "city") String city) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("cities", cityService.findAll());
        String selectCity = cityService.findCorrectNameOfCity(city);
        if (selectCity != null) {
            model.addAttribute("companies", pointOfServiceServices.findAllByCity(selectCity));
            model.addAttribute("select", selectCity);
        } else {
            model.addAttribute("companies", pointOfServiceServices.findAll());
            model.addAttribute("select", "Выберите город");
        }
        model.addAttribute("favourites", userService.getFavourites(principal));
        model.addAttribute("activePage", "main");
        return "main";
    }
}
