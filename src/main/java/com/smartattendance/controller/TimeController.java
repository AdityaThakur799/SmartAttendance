package com.smartattendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeController {

    @GetMapping("/time")
    public String showTimePage() {
        return "time";  // opens time.html
    }
}
