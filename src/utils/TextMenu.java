package src.utils;


import static utils.Texto.*;

public class TextMenu {
    String titulo;
    String[] opciones;
    int ancho = 100;

    public TextMenu(String titulo) {
        this.titulo = titulo;
        setOpciones("Salir");
    }

    public int getAncho() {
        return ancho;
    }

    public void setOpciones(String... nombreOpcion) {
        opciones = new String[nombreOpcion.length + 1];
        int i;
        for (i = 0; i < nombreOpcion.length; i++) {
            opciones[i] = String.format(" %2d) %s", i + 1, nombreOpcion[i]);
        }
        opciones[nombreOpcion.length] = String.format(" %2d) %s", i + 1, "Salir");
        ancho = anchoMenu();

    }

    public void desplegar() {
        System.out.println();
        System.out.println(lineaDoble(ancho));
        String tit = "...::: " + titulo + " :::...";
        System.out.println(centrar(tit, ancho));
        System.out.println();
        for (int i = 0; i < opciones.length; i++) {
            System.out.println(opciones[i]);
        }
        System.out.println(linea(ancho));
        System.out.print("..:: Ingrese número de opción: ");
    }

    public int opSalir() {
        return opciones.length;
    }


    private int anchoMenu() {
        int ancho = 0;
        for (String op : opciones) {
            if (op.length() > ancho) {
                ancho = op.length();
            }
        }
        return Math.max(ancho, 2 * "...::: ".length() + titulo.length());
    }

}
