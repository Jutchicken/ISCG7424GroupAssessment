package com.example.iscg7424groupassessment.admin;

public class UserItem {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;   // "Staff" for the accounts the admin manages

    public UserItem() {
    }

    public UserItem(String id, String username, String fullName, String email, String phone, String role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Customers and admins also live in users/; the admin screen only lists "Staff"
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}