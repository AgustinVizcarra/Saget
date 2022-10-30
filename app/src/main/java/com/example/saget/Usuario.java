package com.example.saget;

public class Usuario {
    private String nombres;
    private String apellidos;
    private String correo;
    private String sexo;
    private String cargo;
    private int rol;
    private String password;

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

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

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario(String nombres, String apellidos, String correo, String sexo,String cargo, int rol, String password) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.cargo = cargo;
        this.sexo = sexo;
        this.rol = rol;
        this.password = password;
    }

    public Usuario() {
    }
}
