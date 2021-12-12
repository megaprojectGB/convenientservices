package com.convenientservices.web.controllers;

import com.convenientservices.web.entities.User;
import com.convenientservices.web.services.UserService;
import com.convenientservices.web.utilities.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserService userService;

    @GetMapping
    public String showIndexPage(Principal principal,
                                Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("activePage", "");
        return "index";
    }

    @GetMapping("/about")
    public String showAboutPage(Principal principal,
                                Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("activePage", "about");
        return "about";
    }

    @GetMapping("/lost")
    public String lostPasswordPage(Principal principal,
                                Model model) {
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("activePage", "about");
        return "lost";
    }

    @PostMapping("/lost")
    public String lostPassPage(@RequestParam String email) {
        userService.getUserByEmail(email);
        return "redirect:/";
    }

    @GetMapping("/change/{code}")
    public String changePasswordPage(@PathVariable("code") String changeCode,
                                     Principal principal,
                                     Model model) {
        model.addAttribute("code", changeCode);
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("activePage", "about");
        return "change";
    }

    @PostMapping("/change")
    public String changeNewPasswordPage(@RequestParam("code") String changeCode,
                                        @RequestParam("password") String password,
                                        @RequestParam("password2") String password2,
                                     Principal principal,
                                     Model model) {
        if (Utils.passwordMatching(password, password2)) {
            userService.getUserByCode(changeCode, password);
        }
        model.addAttribute("username", userService.getFIO(principal));
        model.addAttribute("activePage", "about");
        return "redirect:/main";
    }
}
