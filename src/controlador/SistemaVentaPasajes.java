package controlador;

import src.excepciones.SistemaVentaPasajesException;
import utilidades.IdPersona;
import utilidades.Nombre;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import modelo.*;

public class SistemaVentaPasajes {
    private static SistemaVentaPasajes instance = null;

    private final List<Cliente> clientes;
    private final List<Venta> ventas;
    private final List<Viaje> viajes;
    private final List<Pasajero> pasajeros;

    private SistemaVentaPasajes() {
        clientes = new ArrayList<>();
        ventas = new ArrayList<>();
        viajes = new ArrayList<>();
        pasajeros = new ArrayList<>();
    }

    public static SistemaVentaPasajes getInstance() {
        if (instance == null) {
            instance = new SistemaVentaPasajes();
        }
        return instance;
    }

    public Optional<String> getNombrePasajero(IdPersona idPasajero) {
        Optional<Pasajero> pasajero = findPasajero(idPasajero);
        if (pasajero.isPresent()) {
            return Optional.of(pasajero.get().getNombreCompleto().toString());
        }
        return Optional.empty();
    }

    public Optional<Integer> getMontoVenta(String idDoc, TipoDocumento tipo) {
        Optional<Venta> venta = findVenta(idDoc, tipo);
        if (venta.isPresent()) {
            Integer integer = venta.get().getMonto();
            return Optional.of(integer);
        }
        return Optional.empty();
    }

    public void createCliente(IdPersona id, Nombre nom, String fono, String email) {
        Optional<Cliente> cliente = clientes.stream().filter(c -> c.getId().equals(id)).findFirst();
        if (cliente.isEmpty()) {
            Cliente nuevo = new Cliente(id, nom, fono, email);
            clientes.add(nuevo);
        } else {
            throw new SistemaVentaPasajesException("Ya existe cliente con el id dado");
        }
    }

    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre contacto, String fonoContacto) {
        Optional<Pasajero> pasajero = pasajeros.stream().filter(c -> c.getId().equals(id)).findFirst();
        if (pasajero.isEmpty()) {
            Pasajero nuevo = new Pasajero(id, nom, fono, contacto, fonoContacto);
            pasajeros.add(nuevo);
        } else {
            throw new SistemaVentaPasajesException("Ya existe pasajero con el id dado");
        }
    }

    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracion,
                            String patBus, IdPersona[] idTripulantes, String[] nomComunas) {
        /* Precondiciones: En el arreglo de tripulantes siempre viene primero el auxiliar y luego los conductores
         *                 En el arreglo nomTerminales primero viene el de la salida y luego el de la llegada
         */
        Optional<Bus> busOptional = ControladorEmpresas.getInstance().findBus(patBus);

        if (busOptional.isPresent()) {
            Bus bus = busOptional.get();
            Empresa empresa = bus.getEmpresa();
            Optional<Auxiliar> auxiliarOptional = ControladorEmpresas.getInstance()
                    .findAuxiliar(idTripulantes[0], empresa.getRut());
            if (auxiliarOptional.isPresent()) {
                Auxiliar auxiliar = auxiliarOptional.get();
                Optional<Conductor> conductorOptional = ControladorEmpresas.getInstance()
                        .findConductor(idTripulantes[1], empresa.getRut());
                if (conductorOptional.isPresent()) {
                    Conductor conductor = conductorOptional.get();
                    Optional<Terminal> salidaOptional = ControladorEmpresas.getInstance()
                            .findTerminalPorComuna(nomComunas[0]);
                    if (salidaOptional.isPresent()) {
                        Terminal salida = salidaOptional.get();
                        Optional<Terminal> llegadaOptional = ControladorEmpresas.getInstance()
                                .findTerminalPorComuna(nomComunas[1]);
                        if (llegadaOptional.isPresent()) {
                            Terminal llegada = llegadaOptional.get();
                            Optional<Viaje> existeViaje = findViaje(fecha, hora, patBus);
                            if (existeViaje.isEmpty()) {
                                Viaje viaje = new Viaje(fecha, hora, precio, duracion, bus, auxiliar, conductor, salida, llegada);
                                viajes.add(viaje);
                            } else {
                                throw new SistemaVentaPasajesException("Ya existe viaje con la fecha, hora y patente de bus dados");
                            }
                        } else {
                            throw new SistemaVentaPasajesException("No existe terminal (llegada) en la comuna dada");
                        }
                    } else {
                        throw new SistemaVentaPasajesException("No existe terminal (salida) en la comuna dada");
                    }
                } else {
                    throw new SistemaVentaPasajesException("No existe conductor con el id dado");
                }
            } else {
                throw new SistemaVentaPasajesException("No existe auxiliar con el id dado");
            }
        } else {
            throw new SistemaVentaPasajesException("No existe bus con la patente dada");
        }
    }

    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaViaje,
                            String comunaSalida, String comunaLlegada, IdPersona idCliente, int nroPasajes) {
        Optional<Cliente> cliente = findCliente(idCliente);
        if (cliente.isPresent()) {
            if (getHorariosDisponibles(fechaViaje, comunaSalida, comunaLlegada, nroPasajes).length > 0) {
                if (findVenta(idDoc, tipo).isEmpty()) {
                    ventas.add(new Venta(idDoc, tipo, LocalDate.now(), cliente.get()));
                } else {
                    throw new SistemaVentaPasajesException("Ya existe venta con id y tipo documento dados");
                }
            } else {
                throw new SistemaVentaPasajesException("No existen viajes disponibles para los criterios dados");
            }
        } else {
            throw new SistemaVentaPasajesException("No existe cliente con el id dado");
        }
    }

    public void vendePasaje(String idDoc, TipoDocumento tipo, LocalDate fecha,
                            LocalTime hora, String patBus, int asiento, IdPersona idPasajero) {
        // Buscar venta
        Optional<Venta> venta = findVenta(idDoc, tipo);
        if (venta.isPresent()) {
            // Buscar viaje
            Optional<Viaje> viaje = findViaje(fecha, hora, patBus);
            if (viaje.isPresent()) {
                // Buscar pasajero
                Optional<Pasajero> pasajero = findPasajero(idPasajero);
                if (pasajero.isPresent()) {
                    venta.get().createPasaje(asiento, viaje.get(), pasajero.get());
                } else {
                    throw new SistemaVentaPasajesException("No existe pasajero con el id dado");
                }
            } else {
                throw new SistemaVentaPasajesException("No existe viaje en la fecha, hora y patente de bus dados");
            }
        } else {
            throw new SistemaVentaPasajesException("No existe venta con id y tipo documento dados");
        }
    }

    public String[][] getHorariosDisponibles(LocalDate fecha, String comunaSalida, String comunaLlegada, int nroPasajes) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        return viajes.stream()
                .filter(viaje -> viaje.getFecha().equals(fecha) &&
                        viaje.getTerminalSalida().getDireccion().getComuna().equalsIgnoreCase(comunaSalida) &&
                        viaje.getTerminalLlegada().getDireccion().getComuna().equalsIgnoreCase(comunaLlegada) &&
                        viaje.existeDisponiblilidad(nroPasajes))
                .map(v -> new String[]{
                        v.getBus().getPatente(),
                        v.getHora().format(dtf),
                        String.format("$%,d", v.getPrecio()),
                        String.valueOf(v.getNroAsientosDisponibles())
                })
                .toArray(String[][]::new);
    }

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        Optional<Viaje> elViaje = findViaje(fecha, hora, patBus);
        if (elViaje.isEmpty()) {
            return new String[0];
        }
        return elViaje.get().getAsientos();
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo) {
        Optional<Venta> ventaOptional = findVenta(idDocumento, tipo);
        if (ventaOptional.isPresent()) {
            boolean pagadoOk = ventaOptional.get().pagaMonto();
            if (!pagadoOk) {
                throw new SistemaVentaPasajesException("Pago no realizado, venta ya fue pagada");
            }
        } else {
            throw new SistemaVentaPasajesException("No existe venta con el id y tipo documento dados");
        }
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo, long nroTarjeta) {
        Optional<Venta> ventaOptional = findVenta(idDocumento, tipo);
        if (ventaOptional.isPresent()) {
            boolean pagadoOk = ventaOptional.get().pagaMonto(nroTarjeta);
            if (!pagadoOk) {
                throw new SistemaVentaPasajesException("Pago no realizado, venta ya fue pagada");
            }
        } else {
            throw new SistemaVentaPasajesException("No existe venta con el id y tipo documento dados");
        }
    }

    public String[][] listVentas() {
        //String[] columnas={"ID DOCUMENTO", "TIPO DOCUMENTO", "FECHA", "RUT/PASAPORTE","CLIENTE","CANT BOLETOS","TOTAL VENTA"}
        //{"4585745", "BOLETA", "12/10/2024", "11.111.111-1", "Juan Eduardo Alarcon Rojas", "10", "$35.000"}
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return ventas.stream()
                .map(venta -> new String[]{
                        venta.getIdDocumento(),
                        venta.getTipo().toString(),
                        venta.getFecha().format(dtf),
                        venta.getCliente().getId().toString(),
                        venta.getCliente().getNombreCompleto().toString(),
                        String.valueOf(venta.getPasajes().length),
                        String.format("$%,d", venta.getMontoPagado())
                }).toArray(String[][]::new);
    }

    public String[][] listViajes() {
        DateTimeFormatter fFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH:mm");
        return viajes.stream()
                .map(viaje -> new String[]{
                        viaje.getFecha().format(fFecha),
                        viaje.getHora().format(fHora),
                        viaje.getFechaHoraTermino().format(fHora),
                        String.format("$%,d", viaje.getPrecio()),
                        String.valueOf(viaje.getNroAsientosDisponibles()),
                        viaje.getBus().getPatente(),
                        viaje.getTerminalSalida().getDireccion().getComuna(),
                        viaje.getTerminalLlegada().getDireccion().getComuna()
                }).toArray(String[][]::new);
    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) {
        Optional<Viaje> elViaje = findViaje(fecha, hora, patBus);
        if (elViaje.isEmpty()) {
            throw new SistemaVentaPasajesException("No existe viaje en la fecha, hora y patente de bus dados");
        }
        return elViaje.get().getListaPasajeros();
    }

    /*
    public String[] pasajesAImprimir(String idDoc, TipoDocumento tipo) {
        Optional<Venta> venta = findVenta(idDoc, tipo);
        if (venta.isPresent()) {
            Pasaje[] pasajes = venta.get().getPasajes();
            String[] out = new String[pasajes.length];
            int i = 0;
            for (Pasaje pasaje : pasajes) {
                out[i++] = pasaje.toString();
            }
            return out;
        }
        return null;
    }
     */

    private Optional<Viaje> findViaje(LocalDate fecha, LocalTime hora, String patBus) {
        return viajes.stream()
                .filter(viaje -> viaje.getFecha().equals(fecha) && viaje.getHora().equals(hora) &&
                        viaje.getBus().getPatente().equals(patBus))
                .findFirst();
    }

    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipo) {
        return ventas.stream()
                .filter(venta -> venta.getIdDocumento().equals(idDocumento) &&
                        venta.getTipo().equals(tipo)).findFirst();
    }

    private Optional<Pasajero> findPasajero(IdPersona idPersona) {
        return pasajeros.stream().filter(pasajero -> pasajero.getId().equals(idPersona)).findFirst();
    }

    private Optional<Cliente> findCliente(IdPersona idPersona) {
        return clientes.stream().filter(cliente -> cliente.getId().equals(idPersona)).findFirst();
    }
}
