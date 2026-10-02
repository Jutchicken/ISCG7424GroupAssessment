package com.example.iscg7424groupassessment;

public class BookingRequest {

    private String propertyName;
    private String customerName;
    private String bookingDate;

    public BookingRequest(
            String propertyName,
            String customerName,
            String bookingDate) {

        this.propertyName = propertyName;
        this.customerName = customerName;
        this.bookingDate = bookingDate;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}
