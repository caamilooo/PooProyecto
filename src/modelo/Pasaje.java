package modelo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Pasaje {
    private final int asiento;
    private final long numero;
    //asociaciones
    private final Venta venta;
    private final Pasajero pasajero;
    private final Viaje viaje;

    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        this.asiento = asiento;
        this.venta = venta;
        this.pasajero = pasajero;
        this.viaje = viaje;
        LocalDateTime now = LocalDateTime.now();
        numero = now.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public int getAsiento() {
        return asiento;
    }

    public Venta getVenta() {
        return venta;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Viaje getViaje() {
        return viaje;
    }

    /*
    @Override
    public String toString() {
        String sb = "------------------ PASAJE --------------------\n" +
                "NUMERO DE PASAJE : " + numero + "\n" +
                "FECHA DE VIAJE   : " + viaje.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                "HORA DE VIAJE    : " + viaje.getHora().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" +
                "PATENTE BUS      : " + viaje.getBus().getPatente() + "\n" +
                "ASIENTO          : " + asiento + "\n" +
                "RUT/PASAPORTE    : " + pasajero.getId().toString() + "\n" +
                "NOMBRE PASAJERO  : " + pasajero.getNombreCompleto().toString() + "\n" +
                "----------------------------------------------\n";
        return sb;
    }
     */
}
