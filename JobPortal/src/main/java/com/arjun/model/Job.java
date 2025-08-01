package com.arjun.model;

import java.math.BigDecimal;

public class Job {
    private int id;
    private String title;
    private String company;
    private String description;
    private String location;
    private BigDecimal salary;
    private int recruiterId; // Foreign key reference to `users(id)`

    public Job(int id, String title, String company, String description, String location, BigDecimal salary, int recruiterId) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.recruiterId = recruiterId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setRecruiterId(int recruiterId) {
        this.recruiterId = recruiterId;
    }
}
