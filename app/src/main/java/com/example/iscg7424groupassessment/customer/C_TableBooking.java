package com.example.iscg7424groupassessment.customer;

public class C_TableBooking {

    private String id;
    private String customerId;
    private String customerEmail;
    private int tableNumber;
    private String date;
    private String time;
    private int guests;
    private String status;

    public C_TableBooking() {
    }

    public C_TableBooking(
            String id,
            String customerId,
            String customerEmail,
            int tableNumber,
            String date,
            String time,
            int guests,
            String status
    ) {
        this.id = id;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.tableNumber = tableNumber;
        this.date = date;
        this.time = time;
        this.guests = guests;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        if (customerEmail == null || customerEmail.isEmpty()) {
            return "Unknown";
        }
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public String getStatus() {
        if (status == null || status.isEmpty()) {
            return "Pending";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
