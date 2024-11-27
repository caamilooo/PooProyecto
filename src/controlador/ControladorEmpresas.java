package controlador;

import excepciones.SistemaVentaPasajesException;
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import modelo.*;

public class ControladorEmpresas {
    // Atributos
    private static ControladorEmpresas controlador = null;

    // Asociaciones
    private final List<Empresa> empresas;
    private final List<Bus> buses;
    private final List<Terminal> terminales;

    // Constructor
    private ControladorEmpresas() {
        empresas = new ArrayList<>();
        buses = new ArrayList<>();
        terminales = new ArrayList<>();
    }

    // Metodos
    public static ControladorEmpresas getInstance() {
        if (controlador == null) {
            controlador = new ControladorEmpresas();
        }
        return controlador;
    }

    public void createEmpresa(Rut rut, String nombre, String url) {
        Optional<Empresa> empresaOptional = findEmpresa(rut);
        if (empresaOptional.isEmpty()) {
            Empresa empresa = new Empresa(rut, nombre);
            empresa.setUrl(url);
            empresas.add(empresa);
            return;
        }
        throw new SistemaVentaPasajesException("Existe empresa con el rut dado");
    }

    public void createBus(String patente, String marca, String modelo, int nroFilas,
                          Rut rutEmpresa) {
        Optional<Bus> bus = buses.stream().filter(b -> b.getPatente().equals(patente)).findFirst();
        if (bus.isEmpty()) {
            Optional<Empresa> empresa = empresas.stream().filter(emp -> emp.getRut().equals(rutEmpresa)).findFirst();
            if (empresa.isPresent()) {
                Bus nuevo = new Bus(patente, nroFilas, empresa.get());
                nuevo.setMarca(marca);
                nuevo.setModelo(modelo);
                buses.add(nuevo);
            } else {
                throw new SistemaVentaPasajesException("No existe empresa con el rut dado");
            }
        } else {
            throw new SistemaVentaPasajesException("Ya existe bus con la patente dada");
        }
    }

    public void createTerminal(String nombre, Direccion direccion) {
        if (findTerminal(nombre).isEmpty()) {
            if (findTerminalPorComuna(direccion.getComuna()).isEmpty()) {
                terminales.add(new Terminal(nombre, direccion));
            } else {
                throw new SistemaVentaPasajesException("Ya existe terminal en la comuna dada");
            }
        } else {
            throw new SistemaVentaPasajesException("Ya existe terminal con el nombre dado");
        }
    }

    public void hireConductorForEmpresa(Rut rutEmp, IdPersona id, Nombre nombre, Direccion dir) {
        Optional<Empresa> empresaOptional = findEmpresa(rutEmp);
        if (empresaOptional.isPresent()) {
            boolean contatacionOk = empresaOptional.get().addConductor(id, nombre, dir);
            if (!contatacionOk) {
                throw new SistemaVentaPasajesException("Ya existe tripulante con el id dado en la empresa");
            }
        } else {
            throw new SistemaVentaPasajesException("No existe empresa con el rut dado");
        }
    }

    public void hireAuxiliarForEmpresa(Rut rutEmp, IdPersona id, Nombre nombre, Direccion dir) {
        Optional<Empresa> empresaOptional = findEmpresa(rutEmp);
        if (empresaOptional.isPresent()) {
            boolean contatacionOk = empresaOptional.get().addAuxiliar(id, nombre, dir);
            if (!contatacionOk) {
                throw new SistemaVentaPasajesException("Ya existe tripulante con el id dado en la empresa");
            }
        } else {
            throw new SistemaVentaPasajesException("No existe empresa con el rut dado");
        }
    }

    /*
    public String[] getComunasDeTerminales() {
        ArrayList<String> comunas = new ArrayList<>();
        for (Terminal terminal : terminales) {
            if (!comunas.contains(terminal.getDireccion().getComuna())) {
                comunas.add(terminal.getDireccion().getComuna());
            }
        }
        return comunas.toArray(new String[0]);
    }
     */

    public String[][] listEmpresas() {
        String[][] listado = new String[empresas.size()][6];
        int k = 0;
        for (Empresa empresa : empresas) {
            listado[k][0] = empresa.getRut().toString();
            listado[k][1] = empresa.getNombre();
            listado[k][2] = empresa.getUrl();
            listado[k][3] = String.format("%d", empresa.getTripulantes().length);
            listado[k][4] = String.format("%d", empresa.getBuses().length);
            listado[k][5] = String.format("%d", empresa.getVentas().length);
            k++;
        }
        return listado;
    }

    public String[][] listLlegadasSalidasTerminal(String nombre, LocalDate fecha) {
        Optional<Terminal> terminalOptional = findTerminal(nombre);
        if (terminalOptional.isPresent()) {
            Terminal terminal = terminalOptional.get();

            List<Viaje> viajesLlegandoEnFecha = Arrays.stream(terminal.getLlegadas())
                    .filter(viaje -> viaje.getFechaHoraTermino().toLocalDate().equals(fecha)).toList();
            List<Viaje> viajesSaliendoEnFecha = Arrays.stream(terminal.getSalidas())
                    .filter(viaje -> viaje.getFecha().equals(fecha)).toList();
            List<Viaje> viajesEnFecha = new ArrayList<>(viajesLlegandoEnFecha);
            viajesEnFecha.addAll(viajesSaliendoEnFecha);

            List<Viaje> viajesEnFechaSorted = viajesEnFecha.stream()
                    .sorted(Comparator.comparing(Viaje::getHora)).toList();

            String[][] listado = new String[viajesEnFechaSorted.size()][5];
            int k = 0;
            for (Viaje viaje : viajesEnFechaSorted) {
                if (viaje.getTerminalLlegada().getNombre().equalsIgnoreCase(nombre)) {
                    listado[k][0] = "Llegada";
                    listado[k][1] = viaje.getFechaHoraTermino().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                } else {
                    listado[k][0] = "Salida";
                    listado[k][1] = viaje.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
                }
                listado[k][2] = viaje.getBus().getPatente();
                listado[k][3] = viaje.getBus().getEmpresa().getNombre();
                listado[k][4] = String.format("%d", viaje.getListaPasajeros().length);
                k++;
            }
            return listado;
        } else {
            throw new SistemaVentaPasajesException("No existe un terminal con el nombre dado");
        }
    }

    public String[][] listVentasEmpresa(Rut rut) {
        Optional<Empresa> empresaOptional = findEmpresa(rut);
        if (empresaOptional.isPresent()) {
            Venta[] ventas = empresaOptional.get().getVentas();
            String[][] listado = new String[ventas.length][4];
            int k = 0;
            for (Venta venta : ventas) {
                listado[k][0] = venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                listado[k][1] = venta.getTipo().toString().toLowerCase();
                listado[k][2] = String.format("$%,d", venta.getMontoPagado());
                listado[k][3] = venta.getTipoPago(); // Siempre debería estar pagado
                k++;
            }
            return listado;
        }
        throw new SistemaVentaPasajesException("No existe empresa con el rut dado");
    }

    protected Optional<Empresa> findEmpresa(Rut rut) {
        return empresas.stream().filter(emp -> emp.getRut().equals(rut)).findFirst();
    }

    protected Optional<Bus> findBus(String patente) {
        return buses.stream().filter(b -> b.getPatente().equalsIgnoreCase(patente)).findFirst();
    }

    protected Optional<Terminal> findTerminal(String nombre) {
        return terminales.stream().filter(term -> term.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }

    protected Optional<Terminal> findTerminalPorComuna(String nombreComuna) {
        return terminales.stream()
                .filter(term -> term.getDireccion().getComuna().equalsIgnoreCase(nombreComuna))
                .findFirst();
    }

    protected Optional<Conductor> findConductor(IdPersona idPersona, Rut rutEmpresa) {
        List<Tripulante> tripulanteList = findTripulantes(idPersona, rutEmpresa);
        for (Tripulante tripulante : tripulanteList) {
            if (tripulante.getClass().getSimpleName()
                    .equalsIgnoreCase("Conductor")) {
                return Optional.of((Conductor) (tripulante));
            }
        }
        return Optional.empty();
    }

    protected Optional<Auxiliar> findAuxiliar(IdPersona idPersona, Rut rutEmpresa) {
        List<Tripulante> tripulanteList = findTripulantes(idPersona, rutEmpresa);
        for (Tripulante tripulante : tripulanteList) {
            if (tripulante.getClass().getSimpleName().equalsIgnoreCase("Auxiliar")) {
                return Optional.of((Auxiliar) (tripulante));
            }
        }
        return Optional.empty();
    }

    private List<Tripulante> findTripulantes(IdPersona idPersona, Rut rutEmpresa) {
        Optional<Empresa> empresaOptional = findEmpresa(rutEmpresa);
        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            Tripulante[] tripulantes = empresa.getTripulantes();
            return Arrays.stream(tripulantes)
                    .filter(tripulante -> tripulante.getId().equals(idPersona)).toList();
        }
        return new ArrayList<>();
    }
}
