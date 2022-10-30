package com.example.saget;

public class Usuario {
    private String nombres;
    private String apellidos;
    private String correo;
    private String sexo;
    private String rol;
    private String password;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario(String nombres, String apellidos, String correo, String sexo, String rol, String password) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.sexo = sexo;
        this.rol = rol;
        this.password = password;
    }

    public Usuario() {
    }
}
