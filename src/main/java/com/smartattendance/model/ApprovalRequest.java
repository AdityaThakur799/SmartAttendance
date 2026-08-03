package com.smartattendance.model;

import jakarta.persistence.*;

@Entity
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String date;
    private String issue;
    private String requestedChange;
    private String status;  // Pending, Approved, Rejected

    public ApprovalRequest() {}

    public ApprovalRequest(String studentName, String date, String issue, String requestedChange, String status) {
        this.studentName = studentName;
        this.date = date;
        this.issue = issue;
        this.requestedChange = requestedChange;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }
    public String getRequestedChange() { return requestedChange; }
    public void setRequestedChange(String requestedChange) { this.requestedChange = requestedChange; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
