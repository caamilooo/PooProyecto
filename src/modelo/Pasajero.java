package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

import java.util.*;

public class Pasajero extends Persona {
    //asociación con pasaje
    List<Pasaje> pasajes;
    private Nombre nombreContacto;
    private String fonoContacto;

    public Pasajero(IdPersona id, Nombre nombreCompleto, String telefono, Nombre nombreContacto, String fonoContacto) {
        super(id, nombreCompleto);
        setTelefono(telefono);
        this.nombreContacto = nombreContacto;
        this.fonoContacto = fonoContacto;
        pasajes = new ArrayList<>();
    }

    public Nombre getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(Nombre nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getFonoContacto() {
        return fonoContacto;
    }

    public void setFonoContacto(String fonoContacto) {
        this.fonoContacto = fonoContacto;
    }

    public void addPasaje(Pasaje pasaje) {
        pasajes.add(pasaje);
    }
}
