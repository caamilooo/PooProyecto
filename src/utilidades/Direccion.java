package utilidades;

import java.util.*;

public class Direccion {
    private final String calle;
    private final int numero;
    private final String comuna;

    public Direccion(String calle, int numero, String comuna) {
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
    }

    public String getCalle() {
        return calle;
    }

    public int getNumero() {
        return numero;
    }

    public String getComuna() {
        return comuna;
    }

    @Override
    public String toString() {
        return calle + " " + numero + ", " + comuna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Direccion direccion)) return false;
        return numero == direccion.numero
                && calle.equalsIgnoreCase(direccion.calle)
                && comuna.equalsIgnoreCase(direccion.comuna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle, numero, comuna);
    }
}
