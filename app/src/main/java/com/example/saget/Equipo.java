package com.example.saget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Equipo {
    private String caracteristicas;
    private int disponibilidad;
    private String equiposAdicionales;
    private String estado;
    private String marca;
    private String nombre;
    private int stock;
    private int tipo;
    private Object imagenes;

    public Object getImagenes() {
        return imagenes;
    }

    public void setImagenes(Object imagenes) {
        this.imagenes = imagenes;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getEquiposAdicionales() {
        return equiposAdicionales;
    }

    public void setEquiposAdicionales(String equiposAdicionales) {
        this.equiposAdicionales = equiposAdicionales;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Equipo(int stock, String marca) {
        this.stock = stock;
        this.marca = marca;
    }

    public Equipo() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Equipo(String caracteristicas, int disponibilidad, String equiposAdicionales, String estado, String marca, String nombre, int stock, int tipo,Object imagenes) {
        this.caracteristicas = caracteristicas;
        this.disponibilidad = disponibilidad;
        this.equiposAdicionales = equiposAdicionales;
        this.estado = estado;
        this.marca = marca;
        this.nombre = nombre;
        this.stock = stock;
        this.tipo = tipo;
        this.imagenes = imagenes;
    }
}
