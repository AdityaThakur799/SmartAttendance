package com.smartattendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApprovalController {

    @GetMapping("/approval")
    public String showApprovalPage() {
        return "approval";  // opens approval.html
    }
}
