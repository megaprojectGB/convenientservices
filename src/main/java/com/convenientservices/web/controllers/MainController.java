package com.convenientservices.web.controllers;

import com.convenientservices.web.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CityService cityService;
    private final PointOfServicesServiceImpl pointOfServiceServices;

    @GetMapping()
    public String showMainPage(Principal principal,
                               Model model, @Param(value = "city") String city) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("cities", cityService.findAll());

if(city!=null){
    model.addAttribute("companies", pointOfServiceServices.findAllByCity(city));
    model.addAttribute("select", city);
}
else {
    model.addAttribute("companies", pointOfServiceServices.findAll());
    model.addAttribute("select", "Выберите город");
}
        model.addAttribute("favourites", userService.getFavourites(principal));
        return "main";
    }
}
