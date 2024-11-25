package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Viaje {
    // Atributos
    private final LocalDate fecha;
    private final LocalTime hora;
    // Asociaciones
    private final List<Pasaje> pasajes;
    private final Bus bus;
    private final List<Conductor> conductores;
    private final Auxiliar auxiliar;
    private final Terminal sale, llega;
    private int precio;
    private int duracion; // minutos

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int duracion, Bus bus,
                 Auxiliar auxiliar, Conductor conductor, Terminal sale, Terminal llega) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.duracion = duracion;
        this.bus = bus;
        bus.addViaje(this);
        pasajes = new ArrayList<>();
        conductores = new ArrayList<>();
        conductores.add(conductor);
        conductor.addViaje(this);
        this.auxiliar = auxiliar;
        auxiliar.addViaje(this);
        this.sale = sale;
        sale.addViajeSalida(this);
        this.llega = llega;
        llega.addViajeLlegada(this);
    }

    // Métodos
    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public Bus getBus() {
        return bus;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public LocalDateTime getFechaHoraTermino() {
        LocalDateTime fechaHoraTermino = LocalDateTime.of(fecha, hora);
        return fechaHoraTermino.plusMinutes(duracion);
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void addPasaje(Pasaje pasaje) {
        if (!pasajes.contains(pasaje)) {
            pasajes.add(pasaje);
        }
    }

    // Modificado
    public String[] getAsientos() {
        String[] out = new String[bus.getNroAsientos()];
        for (int i = 0; i < out.length; i++) {
            out[i] = String.valueOf(i + 1);
        }
        int asiento;
        for (Pasaje p : pasajes) {
            asiento = p.getAsiento();
            out[asiento - 1] = "*";
        }
        return out;
    }

    public String[][] getListaPasajeros() {
        String[][] out = new String[pasajes.size()][5];
        Pasajero p;
        int i = 0;
        for (Pasaje pasaje : pasajes) {
            p = pasaje.getPasajero();
            out[i][0] = String.valueOf(pasajes.get(i).getAsiento());
            out[i][1] = p.getId().toString();
            out[i][2] = p.getNombreCompleto().toString();
            out[i][3] = p.getNombreContacto().toString();
            out[i][4] = p.getFonoContacto();
            i++;
        }
        return out;
    }

    public boolean existeDisponiblilidad(int nroAsientos) {
        return bus.getNroAsientos() - pasajes.size() >= nroAsientos;
    }

    public int getNroAsientosDisponibles() {
        int nroAsientosDisponibles = bus.getNroAsientos();
        return nroAsientosDisponibles - pasajes.size();
    }

    public Venta[] getVentas() {
        ArrayList<Venta> ventas = new ArrayList<>();
        Venta venta;
        for (Pasaje pasaje : pasajes) {
            venta = pasaje.getVenta();
            if (venta != null && !ventas.contains(venta)) {
                ventas.add(venta);
            }
        }
        return ventas.toArray(new Venta[0]);
    }

    public void addConductor(Conductor conductor) {
        if (!conductores.contains(conductor)) {
            conductores.add(conductor);
        }
    }

    public Tripulante[] getTripulantes() {
        ArrayList<Tripulante> tripulantes = new ArrayList<>(conductores);
        tripulantes.add(auxiliar);
        return tripulantes.toArray(new Tripulante[0]);
    }

    public Terminal getTerminalSalida() {
        return sale;
    }

    public Terminal getTerminalLlegada() {
        return llega;
    }
}
