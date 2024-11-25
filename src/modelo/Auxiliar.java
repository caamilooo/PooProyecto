package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

import java.util.*;

public class Auxiliar extends Tripulante {
    // Asociacion
    private final ArrayList<Viaje> viajes;

    public Auxiliar(IdPersona id, Nombre nombre, Direccion direccion) {
        super(id, nombre, direccion);
        viajes = new ArrayList<>();
    }

    @Override
    public void addViaje(Viaje viaje) {
        if (!viajes.contains(viaje)) {
            viajes.add(viaje);
        }
    }

    @Override
    public int getNroViajes() {
        return viajes.size();
    }
}
