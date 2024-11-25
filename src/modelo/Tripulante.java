package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

public abstract class Tripulante extends Persona {
    private Direccion direccion;

    public Tripulante(IdPersona id, Nombre nombre, Direccion direccion) {
        super(id, nombre);
        this.direccion = direccion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public abstract void addViaje(Viaje viaje);

    public abstract int getNroViajes();
}
