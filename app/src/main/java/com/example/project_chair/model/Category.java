package com.example.project_chair.model;

public class Category {

    private int id;
    private String name;
    private int image;

    public Category(int id, String name, int image){
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Category(){

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public int getImage(){
        return image;
    }
}
