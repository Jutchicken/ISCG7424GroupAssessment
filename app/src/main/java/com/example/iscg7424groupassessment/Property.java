package com.example.iscg7424groupassessment;

public class Property {
    private String name;
    private String location;
    private String price;
    private int image;
    private String category;
    private String supplier;
    private String imageUri;

    // For Customer
    public Property(String name, String location, String price, int image, String category) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.image = image;
        this.category = category;
    }


    /*
    public Property(String name, String location, String price, int image, String category, String supplier) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.image = image;
        this.category = category;
        this.supplier = supplier;
    }
    */
    public Property(String name, String location, String price, int image, String category, String imageUri) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.image = image;
        this.category = category;
        this.imageUri = imageUri;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
