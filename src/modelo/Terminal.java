package modelo;

import utilidades.Direccion;

import java.io.Serializable;
import java.util.*;

public class Terminal implements Serializable {
    // Relaciones
    private final ArrayList<Viaje> llegadas;
    private final ArrayList<Viaje> salidas;
    // Atributos
    private final String nombre;
    private Direccion direccion;

    // Constructor


    public Terminal(String nombre, Direccion direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        llegadas = new ArrayList<>();
        salidas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void addViajeLlegada(Viaje viaje) {
        if (!llegadas.contains(viaje)) {
            llegadas.add(viaje);
        }
    }

    public void addViajeSalida(Viaje viaje) {
        if (!salidas.contains(viaje)) {
            salidas.add(viaje);
        }
    }

    public Viaje[] getLlegadas() {
        return llegadas.toArray(new Viaje[0]);
    }

    public Viaje[] getSalidas() {
        return salidas.toArray(new Viaje[0]);
    }
}
