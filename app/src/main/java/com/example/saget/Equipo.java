package com.example.saget;

public class Equipo {
    private String caracteristicas;
    private int disponibilidad;
    private String equiposAdicionales;
    private int estado;
    private String marca;
    private int stock;
    private int tipo;

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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
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

    public Equipo(String caracteristicas, int disponibilidad, String equiposAdicionales, int estado, String marca, int stock, int tipo) {
        this.caracteristicas = caracteristicas;
        this.disponibilidad = disponibilidad;
        this.equiposAdicionales = equiposAdicionales;
        this.estado = estado;
        this.marca = marca;
        this.stock = stock;
        this.tipo = tipo;
    }
}
