package com.smartattendance.controller;

import com.smartattendance.service.ApprovalRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ApprovalController {

    private final ApprovalRequestService service;

    public ApprovalController(ApprovalRequestService service) {
        this.service = service;
    }

    @GetMapping("/approval")
    public String viewApprovalPage(Model model) {
        model.addAttribute("requests", service.getAllRequests());
        return "approval";
    }

    @PostMapping("/approval/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        service.approveRequest(id);
        return "redirect:/approval";
    }

    @PostMapping("/approval/reject/{id}")
    public String rejectRequest(@PathVariable Long id) {
        service.rejectRequest(id);
        return "redirect:/approval";
    }
}
