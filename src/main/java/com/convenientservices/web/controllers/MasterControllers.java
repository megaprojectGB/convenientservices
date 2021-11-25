package com.convenientservices.web.controllers;

import com.convenientservices.web.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/master")
public class MasterControllers {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ServiceService serviceService;
    private final ServiceCategoryService serviceCategoryService;

    @GetMapping()
    public String showMasterSettingsPage(Principal principal,
                                         Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("services", userService.getUserDTOByUserName(principal).getMasterServices());
        return "master";
    }

    @GetMapping("/delete/{id}")
    public String deleteServiceByUser(Principal principal,
                                      @PathVariable Long id) {
        userService.deleteServiceByUser(principal, id);
        return "redirect:/master";
    }

    @GetMapping("/new")
    public String newServiceByUser(Principal principal,
                                   Model model) {
        model.addAttribute("categories", serviceCategoryService.findAll());
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("services", serviceService.findAll());
        return "new-service";
    }

    @GetMapping("/new/{id}")
    public String addNewServiceByUser(Principal principal,
                                      @PathVariable Long id) {
        userService.addServiceToUser(principal, id);
        return "redirect:/master";
    }
}
