package com.convenientservices.web.controllers;

import com.convenientservices.web.dto.ServiceDto;
import com.convenientservices.web.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/master")
public class MasterControllers {
    private final UserService userService;
    private final PointOfServiceServices pointOfServiceServices;
    private final ServiceService serviceService;
    private final ServiceCategoryService serviceCategoryService;
    private final BookingService bookingService;

    @GetMapping()
    public String showMasterSettingsPage(Principal principal, Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("services", userService.getUserDTOByUserName(principal).getMasterServices());
        model.addAttribute("pointofservice", userService.getUserDTOByUserName(principal).getMasterPos());
        model.addAttribute("bookings", bookingService.getBookingsMaster(userService.getUserDTOByUserName(principal).getId()));
        return "master";
    }

    @GetMapping("/delete/{id}")
    public String deleteServiceByUser(Principal principal,
                                      @PathVariable Long id) {
        userService.deleteServiceByUser(principal, id);
        return "redirect:/master";
    }

    @GetMapping("/delete/pos/{id}")
    public String deletePosByMaster(Principal principal,
                                    @PathVariable Long id) {
        pointOfServiceServices.deletePosByUser(principal, id);
        return "redirect:/master";
    }

    @GetMapping("/new")
    public String newServiceByUser(Principal principal,
                                   Model model,
                                   @Param(value = "category") String category) {
        model.addAttribute("categories", serviceCategoryService.findAll());
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));

        List<ServiceDto> serviceDtos = serviceService.findAll();
        if (category != null) {
            serviceDtos = serviceDtos.stream()
                    .filter(serviceDto -> serviceDto.getCategory().getName().equals(category))
                    .collect(Collectors.toList());
        }
        model.addAttribute("services", serviceDtos);

        return "new-service";
    }

    @GetMapping("/new/{id}")
    public String addNewServiceByUser(Principal principal,
                                      @PathVariable Long id) {
        userService.addServiceToUser(principal, id);
        return "redirect:/master";
    }

    @GetMapping("/create")
    public String createServiceByUser(Principal principal,
                                      Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("categories", serviceCategoryService.findAll());
        return "create_service";
    }

    @PostMapping("/create")
    public String saveNewServiceByUser(@RequestParam String name,
                                       @RequestParam String duration,
                                       @RequestParam String categoryId) {
        serviceService.createNewCategoryService(name, duration, categoryId);
        return "redirect:/master/new";
    }
}
