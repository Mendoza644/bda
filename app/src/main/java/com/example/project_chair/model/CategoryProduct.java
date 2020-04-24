package com.example.project_chair.model;






public class CategoryProduct {

    private int id;
    private String name;
    private String description;
    private String price;
    private String image;
    private  String brand;


    public CategoryProduct(int id, String name, String image, String description, String price){
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;

    }

    public CategoryProduct(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getName() {
        return name;
    }
    public String getImage(){
        return image;
    }
    public String getPrice() {
        return price;
    }


}


