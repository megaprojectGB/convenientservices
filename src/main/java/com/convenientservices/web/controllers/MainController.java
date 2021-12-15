package com.convenientservices.web.controllers;

import com.convenientservices.web.enums.UserActivationState;
import com.convenientservices.web.services.CategoryService;
import com.convenientservices.web.services.CityService;
import com.convenientservices.web.services.PointOfServiceServices;
import com.convenientservices.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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
                                Model model,
                                @Param(value = "selectcity") String city,
                                @Param(value = "active") UserActivationState active,
                                @Param(value = "category") String category,
                                @Param(value = "pos") String pos) {
        Map<String, String> params = new HashMap<>();
        String selectCity = cityService.findCorrectNameOfCity(city);
        String selectCategory = categoryService.findCorrectNameOfCategory(category);

        params.put("city", selectCity);
        params.put("category", selectCategory);
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("active", active);
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("cities", cityService.findAll());

        if (selectCity != null) {
            model.addAttribute("select", selectCity);
        } else {
            model.addAttribute("select", "Выберите город");
        }
        if (selectCategory != null) {
            model.addAttribute("selectcategory", selectCategory);
        } else {
            model.addAttribute("selectcategory", "Выберите категорию");
        }

        if (pos != null) {
            model.addAttribute("companies", pointOfServiceServices.findAllByNameLike(pos, selectCity, selectCategory));
        } else {
            model.addAttribute("companies", pointOfServiceServices.findAll(params));
        }

        model.addAttribute("favourites", userService.getFavourites(principal));
        model.addAttribute("activePage", "main");
        return "main";
    }

}
