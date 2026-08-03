package com.smartattendance.service;

import com.smartattendance.model.ApprovalRequest;
import com.smartattendance.repository.ApprovalRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalRequestService {

    private final ApprovalRequestRepository repository;

    public ApprovalRequestService(ApprovalRequestRepository repository) {
        this.repository = repository;
    }

    public List<ApprovalRequest> getAllRequests() {
        return repository.findAll();
    }

    public void approveRequest(Long id) {
        ApprovalRequest req = repository.findById(id).orElse(null);
        if (req != null) {
            req.setStatus("Approved");
            repository.save(req);
        }
    }

    public void rejectRequest(Long id) {
        ApprovalRequest req = repository.findById(id).orElse(null);
        if (req != null) {
            req.setStatus("Rejected");
            repository.save(req);
        }
    }

    public void addDummyRequests() {
        if (repository.count() == 0) {
            repository.save(new ApprovalRequest(
                    "Aditya Thakur",
                    "23 Nov 2025",
                    "Marked absent incorrectly",
                    "Mark as present",
                    "Pending"
            ));

            repository.save(new ApprovalRequest(
                    "Harsh Patel",
                    "18 Nov 2025",
                    "Checkout missing",
                    "Add checkout time 3:45 PM",
                    "Pending"
            ));

            repository.save(new ApprovalRequest(
                    "Aishwarya Jain",
                    "25 Nov 2025",
                    "Wrong check-in time",
                    "Correct check-in to 9:05 AM",
                    "Pending"
            ));
        }
    }
}
