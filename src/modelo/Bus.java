package modelo;

import java.io.Serializable;
import java.util.*;

public class Bus implements Serializable {
    // Atributos
    private final String patente;
    private final int nroAsientos;
    // Asociaciones
    private final List<Viaje> viajes;
    private String marca;
    private String modelo;
    private final Empresa empresa;

    public Bus(String patente, int nroAsientos, Empresa empresa) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.empresa = empresa;
        this.empresa.addBus(this);
        viajes = new ArrayList<>();
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getNroAsientos() {
        return nroAsientos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void addViaje(Viaje viaje) {
        if (!viajes.contains(viaje)) {
            viajes.add(viaje);
        }
    }

    public Viaje[] getViajes() {
        return viajes.toArray(new Viaje[0]);
    }
}
