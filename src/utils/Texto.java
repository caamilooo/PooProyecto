package utils;

import java.util.*;

public class Texto {
    public static String centrar(String s, int ancho) {
        if (s.length() > ancho) return s;
        char[] left = new char[(ancho - s.length()) / 2];
        char[] right = new char[ancho - s.length() - left.length];
        Arrays.fill(left, ' ');
        Arrays.fill(right, ' ');
        return new String(left) + s + new String(right);
    }

    public static String linea(int largo) {
        char[] linea = new char[largo];
        Arrays.fill(linea, '-');
        return new String(linea);
    }

    public static String lineaDoble(int largo) {
        char[] linea = new char[largo];
        Arrays.fill(linea, '=');
        return new String(linea);
    }

}
