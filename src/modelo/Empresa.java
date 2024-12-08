package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;

import java.io.Serializable;
import java.util.*;

public class Empresa implements Serializable {
    // Asociaciones
    private final List<Conductor> conductores;
    private final List<Auxiliar> auxiliares;
    private final List<Bus> buses;
    private final Rut rut;
    private final String nombre;
    private String url;

    // Constructor
    public Empresa(Rut rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
        conductores = new ArrayList<>();
        auxiliares = new ArrayList<>();
        buses = new ArrayList<>();
    }

    // Métodos

    public Rut getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addBus(Bus bus) {
        if (!buses.contains(bus)) {
            buses.add(bus);
        }
    }

    public Bus[] getBuses() {
        return buses.toArray(new Bus[0]);
    }

    public boolean addConductor(IdPersona idPersona, Nombre nombre, Direccion direccion) {
        if (getTripulante(idPersona).isEmpty()) {
            conductores.add(new Conductor(idPersona, nombre, direccion));
            return true;
        }
        return false;
    }

    public boolean addAuxiliar(IdPersona idPersona, Nombre nombre, Direccion direccion) {
        if (getTripulante(idPersona).isEmpty()) {
            auxiliares.add(new Auxiliar(idPersona, nombre, direccion));
            return true;
        }
        return false;
    }

    public Tripulante[] getTripulantes() {
        Tripulante[] tripulantes = new Tripulante[conductores.size() + auxiliares.size()];
        for (int i = 0; i < conductores.size(); i++) {
            tripulantes[i] = conductores.get(i);
        }
        for (int i = 0; i < auxiliares.size(); i++) {
            tripulantes[i + conductores.size()] = auxiliares.get(i);
        }
        return tripulantes;
    }

    public Venta[] getVentas() {
        ArrayList<Venta> ventas = new ArrayList<>();
        Viaje[] viajes;
        Venta[] ventasViaje;

        for (Bus bus : buses) {
            viajes = bus.getViajes();
            for (Viaje viaje : viajes) {
                ventasViaje = viaje.getVentas();
                for (Venta venta : ventasViaje) {
                    if (!ventas.contains(venta)) {
                        ventas.add(venta);
                    }
                }
            }
        }
        return ventas.toArray(new Venta[0]);
    }

    private Optional<Tripulante> getTripulante(IdPersona idPersona) {
        for (Conductor conductor : conductores) {
            if (conductor.getId().equals(idPersona)) {
                return Optional.of(conductor);
            }
        }
        for (Auxiliar auxiliar : auxiliares) {
            if (auxiliar.getId().equals(idPersona)) {
                return Optional.of(auxiliar);
            }
        }
        return Optional.empty();
    }
}
