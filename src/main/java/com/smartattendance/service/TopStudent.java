package com.smartattendance.service;

public class TopStudent {

    private int id;
    private String name;
    private String rollNo;
    private double percent;

    public TopStudent(int id, String name, String rollNo, double percent) {
        this.id = id;
        this.name = name;
        this.rollNo = rollNo;
        this.percent = percent;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getRollNo() { return rollNo; }
    public double getPercent() { return percent; }

    // Setters (optional)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public void setPercent(double percent) { this.percent = percent; }
}
