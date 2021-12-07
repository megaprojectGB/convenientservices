package com.convenientservices.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    @GetMapping()
    public String showBookingPage (Principal principal,
                                Model model,
                                @Param(value = "posId") Long posId) {
        return "booking";
    }
}
