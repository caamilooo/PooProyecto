package modelo;

public abstract class Pago {
    private final int monto;

    public Pago(int monto) {
        this.monto = monto;
    }

    public int getMonto() {
        return monto;
    }
}
