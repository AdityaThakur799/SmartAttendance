package com.smartattendance.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "roll_no")
    private String rollNo;

    @Column(name = "qr_code")
    private String qrCode;

    // Full constructor including QR
    public Student(int id, String name, String rollNo, String qrCode) {
        this.id = id;
        this.name = name;
        this.rollNo = rollNo;
        this.qrCode = qrCode;
    }

    // Constructor for data fetch (3 args)
    public Student(int id, String name, String rollNo) {
        this.id = id;
        this.name = name;
        this.rollNo = rollNo;
    }

    // Constructor for registration (2 args)
    public Student(String name, String rollNo) {
        this.name = name;
        this.rollNo = rollNo;
    }

    // Default empty constructor — REQUIRED BY JPA
    public Student() {}

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
