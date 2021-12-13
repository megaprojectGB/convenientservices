package com.convenientservices.web.controllers;

import com.convenientservices.web.entities.Booking;
import com.convenientservices.web.services.BookingService;
import com.convenientservices.web.services.MailSenderService;
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
@RequestMapping("/orders")
public class OrderController {
    private final UserService userService;
    private final BookingService bookingService;
    private final MailSenderService mailSenderService;


    @GetMapping
    public String showOrderPage (Principal principal,
                                 Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("userDTO", userService.getUserDTOByUserName(principal));
        model.addAttribute("bookings", bookingService.getGoodBookings(principal));
        model.addAttribute("oldBookings", bookingService.getOldBookings(principal));
        model.addAttribute("activePage", "orders");
        return "orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteById (@PathVariable Long id, Principal principal) throws Exception {
        Booking booking = bookingService.findById(id);
        mailSenderService.sendBookingCancellationMessage(booking);
        bookingService.deleteById(id);
        return "redirect:/orders";
    }

}
