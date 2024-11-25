package modelo;

public class PagoTarjeta extends Pago {
    private final long nroTarjeta;

    public PagoTarjeta(long nroTarjeta, int monto) {
        super(monto);
        this.nroTarjeta = nroTarjeta;
    }

    public long getNroTarjeta() {
        return nroTarjeta;
    }
}
