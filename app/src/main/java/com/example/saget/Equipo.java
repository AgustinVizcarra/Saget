package com.example.saget;

public class Equipo {
    private int stock;
    private String marca;

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
}
