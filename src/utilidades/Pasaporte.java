package utilidades;

import java.io.Serializable;
import java.util.*;

public class Pasaporte implements IdPersona, Serializable {
    private final String numero;
    private final String nacionalidad;

    private Pasaporte(String numero, String nacionalidad) {
        this.numero = numero;
        this.nacionalidad = nacionalidad;
    }

    private static boolean isAlfa(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean esValido(String numero, String nacionalidad) {
        //revisar que sea alfanumérico

        return numero != null
                && nacionalidad != null
                && isAlfa(numero)
                && isAlfa(nacionalidad)
                && !numero.isEmpty()
                && !nacionalidad.isEmpty();
    }

    public static Pasaporte of(String numero, String nacionalidad) {
        if (esValido(numero, nacionalidad)) {
            return new Pasaporte(numero, nacionalidad);
        }
        return null;
    }

    public String getNumero() {
        return numero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", numero, nacionalidad);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pasaporte pasaporte)) return false;
        return numero.equals(pasaporte.numero) && nacionalidad.equals(pasaporte.nacionalidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, nacionalidad);
    }
}
