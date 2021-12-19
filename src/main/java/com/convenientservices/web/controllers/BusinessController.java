package com.convenientservices.web.controllers;

import com.convenientservices.web.dto.PointOfServiceDto;
import com.convenientservices.web.services.BookingService;
import com.convenientservices.web.services.CategoryService;
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
@RequestMapping("/business")
public class BusinessController {
    private final UserService userService;
    private final PointOfServiceServices pointOfServiceServices;
    private final CategoryService categoryService;
    private final BookingService bookingService;


    @GetMapping()
    public String showBusinessSettingsPage (Principal principal,
                                            Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("services", userService.getUserDTOByUserName(principal).getMasterServices());
        model.addAttribute("pointofservices", pointOfServiceServices.findAllByUserBoss(principal));
        return "business";
    }

    @GetMapping("/delete/{id}")
    public String deleteUserPos (Principal principal,
                                 Model model,
                                 @PathVariable Long id) {

        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("services", userService.getUserDTOByUserName(principal).getMasterServices());
        model.addAttribute("pointofservices", pointOfServiceServices.findAllByUserBoss(principal));
        return "business";
    }

    @GetMapping("/edit/{id}")
    public String editUserPos (Principal principal,
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
    public String saveEditUserPos (Principal principal,
                                   @ModelAttribute("pos") PointOfServiceDto posDto,
                                   @ModelAttribute("category") String category) {
        pointOfServiceServices.editNewPos(posDto, category, principal);
        return "redirect:/business";
    }

    @GetMapping("/new")
    public String newUserPos (Principal principal,
                              Model model) {

        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("pos", new PointOfServiceDto());
        model.addAttribute("posCategory", categoryService.findAll());
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "new_business";
    }

    @PostMapping("/new")
    public String newPos (Principal principal,
                          @ModelAttribute("pos") PointOfServiceDto posDto,
                          @ModelAttribute("category") String category) {
        pointOfServiceServices.saveNewPos(posDto, category, principal);
        return "redirect:/business";
    }

    @GetMapping("/pos/{id}")
    public String showPos (Principal principal,
                           Model model,
                           @PathVariable Long id) {
        model.addAttribute("posId", id);
        model.addAttribute("bookings", bookingService.findAllByPosId(id));
        model.addAttribute("masters", pointOfServiceServices.getMastersForPos(id));
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "business_pos";
    }


    @GetMapping("/deletemasterfrompos")
    public String deleteMasterPos (@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "posId") Long posId) {
        pointOfServiceServices.deleteMasterFromPos(id, posId);
        return "redirect:/business/pos/".concat(posId.toString());
    }

    @GetMapping("/addmaster")
    public String addMasterToPos (Principal principal,
                                  Model model,
                                  @RequestParam(name = "posId") Long posId,
                                  @Param(value = "master") String master,
                                  @Param(value = "service") String service

    ) {
        Map<String, String> params = new HashMap<>();
        params.put("name", master);
        params.put("service", service);
        params.put("role", "ROLE_MASTER");

        model.addAttribute("posId", posId);
        model.addAttribute("masters", userService.findAll(params));
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "business_add_master";
    }

    @GetMapping("/addmastertopos")
    public String addMasterToPos (Principal principal,
                                  Model model,
                                  @RequestParam(name = "posId") Long posId,
                                  @RequestParam(name = "id") Long masterId) {
        userService.addMasterToPos(posId, masterId);
        return "redirect:/business/addmaster?posId=".concat(String.valueOf(posId));
    }
}
