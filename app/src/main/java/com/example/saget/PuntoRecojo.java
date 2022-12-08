package com.example.saget;

import java.io.Serializable;

public class PuntoRecojo implements Serializable {
    private String descripcion;
    private String coordenadas;
    private Object imagenes;
    private String key;
    private int estado;

    public PuntoRecojo(String descripcion, String coordenadas, Object imagenes, String key, int estado) {
        this.descripcion = descripcion;
        this.coordenadas = coordenadas;
        this.imagenes = imagenes;
        this.key = key;
        this.estado = estado;
    }

    public PuntoRecojo() {
    }

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
