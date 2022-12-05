package com.example.saget;

public class EquipoCompleto {
    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public EquipoCompleto(Equipo equipo, String key) {
        this.equipo = equipo;
        this.key = key;
    }

    private Equipo equipo;
    private String key;
}
