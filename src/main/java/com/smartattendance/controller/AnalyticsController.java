package com.smartattendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnalyticsController {

    @GetMapping("/analytics")
    public String showAnalytics() {
        return "analytics";  // opens analytics.html
    }
}
