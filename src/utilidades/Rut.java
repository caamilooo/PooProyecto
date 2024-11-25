package utilidades;

public class Rut implements IdPersona {
    private final int rut;
    private final char dv;

    private Rut(int rut, char dv) {
        this.rut = rut;
        this.dv = dv;
    }

    public static boolean esValido(String rut) {
        // rut puede contener puntos o no.
        // Se verifica que solo existan numeros en la primera parte y el último caracter sea digito o k
        // Se verifica que este presente el digito verificador separado por guion
        if (rut.indexOf("-") != rut.length() - 2 ||
                !(rut.toLowerCase().charAt(rut.length() - 1) == 'k' ||
                        Character.isDigit(rut.charAt(rut.length() - 1)))) {
            return false;
        }
        String[] partes = rut.split("-");
        String numeroStr = partes[0].replace(".", "");
        String dvStr = partes[1];
        int suma = 0, factor = 2, resultado;

        try {
            for (int i = numeroStr.length() - 1; i >= 0; i--) {
                suma += Integer.parseInt(numeroStr.substring(i, i + 1)) * factor;
                factor = (factor + 1) % 8;
                if (factor == 0)
                    factor = 2;
            }

            resultado = 11 - suma % 11;
            return resultado == 11 && dvStr.equals("0") ||
                    resultado == 10 && dvStr.equalsIgnoreCase("k") ||
                    Character.isDigit(dvStr.charAt(0)) && resultado == Integer.parseInt(dvStr);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Rut of(String rut) {
        try {
            if (esValido(rut)) {
                String[] partes = rut.split("-");
                String numeroStr = partes[0].replace(".", "");
                String dvStr = partes[1];
                return new Rut(Integer.parseInt(numeroStr), dvStr.charAt(0));
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public int getRut() {
        return rut;
    }

    public char getDv() {
        return dv;
    }

    public boolean esValido() {
        return esValido(this.toString());
    }

    @Override
    public String toString() {
        return String.format("%,d-%s", rut, dv).replace(",", ".");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rut rut1)) return false;
        return rut == rut1.rut;
    }

}
