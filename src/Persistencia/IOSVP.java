package Persistencia;

import com.sun.jdi.ObjectReference;
import excepciones.SistemaVentaPasajesException;
import modelo.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class IOSVP {

    private static final String datosIniciales = "SVPDatosIniciales.txt";
    private static final String objetos = "SVPObjetos.obj";

    public Object[] readDatosIniciales() throws SistemaVentaPasajesException {
        return null;
    }

    public Object[] readControladores() throws SistemaVentaPasajesException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objetos))) {
            return (Object[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SistemaVentaPasajesException(("Error al leer los controladores: " + e.getMessage()));
        }
    }

    public void saveControladores(Object[] controladores) throws SistemaVentaPasajesException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(datosIniciales))){
            oos.writeObject(controladores);
        }catch (IOException e){
            throw new SistemaVentaPasajesException(("Error al guardar los datos del sistema" + e.getMessage()));
        }
    }
}

