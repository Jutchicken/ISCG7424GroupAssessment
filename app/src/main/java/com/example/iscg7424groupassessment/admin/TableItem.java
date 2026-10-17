package com.example.iscg7424groupassessment.admin;

public class TableItem {
    private String id;
    private int tableNumber;
    private int seats;

    public TableItem(){

    }

    public TableItem(String id, int tableNumber, int seats){
        this.id = id;
        this.tableNumber = tableNumber;
        this.seats = seats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
