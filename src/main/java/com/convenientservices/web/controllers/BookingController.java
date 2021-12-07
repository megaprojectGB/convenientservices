package com.convenientservices.web.controllers;

import com.convenientservices.web.entities.User;
import com.convenientservices.web.services.PointOfServiceServices;
import com.convenientservices.web.services.UserService;
import com.convenientservices.web.utilities.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {
    private final UserService userService;
    private final PointOfServiceServices pointOfServiceServices;

    @GetMapping()
    public String showBookingPage(Principal principal,
                                  Model model,
                                  @RequestParam Long posId,
                                  @RequestParam(required = false) String calendar,
                                  @RequestParam(required = false) Long masterId) {
        LocalDate selectedDate = LocalDate.now();
        if(calendar != null) {
            try {
                selectedDate = Utils.getLocalDateFromString(calendar);
            } catch (Exception e) {
                return "redirect:/booking?posId=".concat(String.valueOf(posId));
            }

        }
        List<User> masters = pointOfServiceServices.getMastersForPos(posId);
        if (masterId == null && !masters.isEmpty()) {
            masterId = masters.get(0).getId();
        }
        model.addAttribute("masterId", masterId);
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("maxDate", LocalDate.now().plusMonths(2));
        model.addAttribute("masters", pointOfServiceServices.getMastersForPos(posId));
        model.addAttribute("posId", posId);
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        return "booking";
    }
}
