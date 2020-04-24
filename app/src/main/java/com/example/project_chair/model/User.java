package com.example.project_chair.model;

public class User {

    private String  apellido;
    private String nombre;
    private String correo;

    public User(String nombre, String apellido, String correo){
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
    }

    public String getName() {
        return nombre;
    }
    public void setName(String nombre){
        this.nombre = nombre;
    }

    public String getSurname() {
        return apellido;
    }
    public void setSurname(String apellido){
        this.apellido = apellido;
    }
    public String getEmail() {
        return correo;
    }
    public void setEmail(String correo){
        this.correo = correo;
    }
}


