package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

import java.util.*;

public abstract class Persona {
    private IdPersona id;
    private Nombre nombreCompleto;
    private String telefono;

    public Persona(IdPersona id, Nombre nombreCompleto) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
    }

    public IdPersona getId() {
        return id;
    }

    public void setId(IdPersona id) {
        this.id = id;
    }

    public Nombre getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(Nombre nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona persona)) return false;
        return id.equals(persona.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
