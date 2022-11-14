package com.example.saget;

public class SolicitudDePrestamo {
    private String usuario;
    private String equipo;
    private String tiempoPrestamo;
    private String curso;
    private String programas;
    private String motivo;
    private String detalles;
    private String Foto;
    private String estado;
    private String observacion;

    public SolicitudDePrestamo() {
    }

    public SolicitudDePrestamo(String usuario, String equipo, String tiempoPrestamo, String curso, String programas, String motivo, String detalles, String foto, String estado, String observacion) {
        this.usuario = usuario;
        this.equipo = equipo;
        this.tiempoPrestamo = tiempoPrestamo;
        this.curso = curso;
        this.programas = programas;
        this.motivo = motivo;
        this.detalles = detalles;
        Foto = foto;
        this.estado = estado;
        this.observacion = observacion;
    }

    public String getObservacion() {
        return observacion;
    }


    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getTiempoPrestamo() {
        return tiempoPrestamo;
    }

    public void setTiempoPrestamo(String tiempoPrestamo) {
        this.tiempoPrestamo = tiempoPrestamo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getProgramas() {
        return programas;
    }

    public void setProgramas(String programas) {
        this.programas = programas;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
