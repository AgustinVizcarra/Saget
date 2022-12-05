package com.example.saget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {
    private String nombres;
    private String apellidos;
    private String correo;
    private String sexo;
    private String cargo;
    private int rol;
    private String password;
    private ArrayList<Object> foto;

    public String getImgurl() {
        return imgurl;
    }

    public ArrayList<Object> getFoto() {
        return foto;
    }

    public void setFoto(ArrayList<Object> foto) {
        this.foto = foto;
    }

    public Usuario(String nombres, String apellidos, String correo, String sexo, String cargo, int rol, String password, ArrayList<Object>  foto) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.sexo = sexo;
        this.cargo = cargo;
        this.rol = rol;
        this.password = password;
        this.foto = foto;
    }


    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    private String imgurl;

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

    public Usuario(String nombres, String apellidos, String correo, String sexo,String cargo, int rol, String password,String imgurl) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.cargo = cargo;
        this.sexo = sexo;
        this.rol = rol;
        this.password = password;
        this.imgurl=imgurl;
    }

    public Usuario() {
    }
}
