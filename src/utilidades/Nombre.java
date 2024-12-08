package utilidades;

import java.io.Serializable;

public class Nombre implements Serializable {
    private Tratamiento tratamiento;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public Nombre(Tratamiento tratamiento, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.tratamiento = tratamiento;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String toString() {
        return tratamiento + " " + nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
}
