package com.example.iscg7424groupassessment.admin;

public class TableBooking {
    private String id;
    private String customerId;
    private String customerName;
    private int tableNumber;
    private String date;
    private String time;
    private int guests;
    private String status;

    public TableBooking(){

    }

    public TableBooking(String id, String customerId, String customerName, int tableNumber, String date, String time, int guests, String status){
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
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

    public String getCustomerName() {
        if(customerName == null || customerName.isEmpty()){
            return "Unknown";
        }
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
        if(status == null || status.isEmpty()){
            return "Pending";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
