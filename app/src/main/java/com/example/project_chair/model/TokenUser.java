package com.example.project_chair.model;

public class TokenUser {

    private String  deviceToken;
    private String dispositivo;
    private String idUsuario;

    public TokenUser(String deviceToken, String dispositivo, String idUsuario){
        this.deviceToken = deviceToken;
        this.dispositivo = dispositivo;
        this.idUsuario = idUsuario;
    }



    public String getDeviceToken() {
        return deviceToken;
    }
    public void setDeviceToken(String deviceToken){
        this.deviceToken = deviceToken;
    }

    public String getDispositivo() {
        return dispositivo;
    }
    public void setDispositivo(String dispositivo){
        this.dispositivo = dispositivo;
    }
    public String getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
    }
}

