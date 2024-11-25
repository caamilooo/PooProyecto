package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

import java.util.*;

public class Cliente extends Persona {
    // Asociación con venta
    List<Venta> ventas;
    // Atributos
    private String email;

    public Cliente(IdPersona id, Nombre nombreCompleto, String telefono, String email) {
        super(id, nombreCompleto);
        super.setTelefono(telefono);
        this.email = email;
        ventas = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addVenta(Venta venta) {
        ventas.add(venta);
    }
}
