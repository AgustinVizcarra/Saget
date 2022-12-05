package com.example.saget;

public class PuntoRecojo {
    private String descripcion;
    private String coordenadas;
    private Object imagenes;
    private String key;
    private int estado;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Object getImagenes() {
        return imagenes;
    }

    public void setImagenes(Object imagenes) {
        this.imagenes = imagenes;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
