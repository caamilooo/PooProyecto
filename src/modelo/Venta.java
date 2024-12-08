package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Venta implements Serializable {
    // Atributos
    private final String idDocumento;
    private final TipoDocumento tipo;
    private final LocalDate fecha;

    // Relaciones
    private final Cliente cliente;
    private final List<Pasaje> pasajes;
    private Pago pago;

    public Venta(String idDocumento, TipoDocumento tipo, LocalDate fecha, Cliente cliente) {
        this.idDocumento = idDocumento;
        this.tipo = tipo;
        this.fecha = fecha;
        this.cliente = cliente;
        cliente.addVenta(this);
        pasajes = new ArrayList<>();
        pago = null;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero) {
        Pasaje pasaje = new Pasaje(asiento, viaje, pasajero, this);
        pasajes.add(pasaje);
        viaje.addPasaje(pasaje);
    }

    public Pasaje[] getPasajes() {
        return pasajes.toArray(new Pasaje[0]);
    }

    public int getMonto() {
        int monto = 0;
        for (Pasaje pasaje : pasajes) {
            monto += pasaje.getViaje().getPrecio();
        }
        return monto;
    }

    public boolean pagaMonto() {
        if (pago == null) {
            pago = new PagoEfectivo(getMonto());
            return true;
        }
        return false;
    }

    public boolean pagaMonto(long nroTarjeta) {
        if (pago == null) {
            pago = new PagoTarjeta(nroTarjeta, getMonto());
            return true;
        }
        return false;
    }

    public int getMontoPagado() {
        if (pago != null) {
            return pago.getMonto();
        }
        return 0;
    }

    public String getTipoPago() {
        if (pago != null) {
            String tipoPago = pago.getClass().getSimpleName();
            return tipoPago.substring(0, 4) + " " + tipoPago.substring(4);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venta venta)) return false;
        return Objects.equals(idDocumento, venta.idDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idDocumento);
    }
}
