package com.smartattendance.controller;

import com.smartattendance.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {

        boolean isValid = loginService.authenticate(username, password);

        if (isValid) {
            model.addAttribute("message", "✅ Login Successful!");
            return "redirect:/dashboard";
            // go to dashboard page
        } else {
            model.addAttribute("error", "❌ Invalid username or password!");
            return "login";       // stay on login page
        }
    }
}
