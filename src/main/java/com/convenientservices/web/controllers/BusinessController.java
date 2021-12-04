package com.convenientservices.web.controllers;

import com.convenientservices.web.dto.PointOfServiceDto;
import com.convenientservices.web.services.CategoryService;
import com.convenientservices.web.services.PointOfServiceServices;
import com.convenientservices.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/business")
public class BusinessController {
    private final UserService userService;
    private final PointOfServiceServices pointOfServiceServices;
    private final CategoryService categoryService;

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
        PointOfServiceDto pos = pointOfServiceServices.getPointForEdit(id);
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("pos", pos);
        model.addAttribute("posCategory", categoryService.findAll());
        model.addAttribute("selector", pos.getSelector());
        return "edit_business";
    }

    @PostMapping("/edit")
    public String saveEditUserPos(Principal principal,
                                  @ModelAttribute("pos") PointOfServiceDto posDto,
                                  @ModelAttribute("category") String category) {
        pointOfServiceServices.editNewPos(posDto, category, principal);
        return "redirect:/business";
    }

    @GetMapping("/new")
    public String newUserPos(Principal principal,
                             Model model) {

        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("pos", new PointOfServiceDto());
        model.addAttribute("posCategory", categoryService.findAll());
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "new_business";
    }

    @PostMapping("/new")
    public String newPos(Principal principal,
                         @ModelAttribute("pos") PointOfServiceDto posDto,
                         @ModelAttribute("category") String category) {
        pointOfServiceServices.saveNewPos(posDto, category, principal);
        return "redirect:/business";
    }
}
