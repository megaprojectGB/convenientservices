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
@RequestMapping("/favourite")
public class FavouriteServicesController {
    private final UserService userService;
    private final PointOfServiceServices pos;

    @GetMapping
    public String showFavouritePage(Principal principal,
                                    Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("companies", userService.getUserDTOByUserName(principal).getFavoriteCompanies());
        return "favourite";
    }

    @GetMapping("/delete/{id}")
    public String deleteFavouriteCompany(Principal principal,
                                         @PathVariable Long id) {
        pos.deleteFavouriteCompanyByUser(principal, id);
        return "redirect:/favourite";
    }
}
