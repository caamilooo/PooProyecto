package vista;

import controlador.ControladorEmpresas;
import controlador.SistemaVentaPasajes;
import excepciones.SVPExepction;
import modelo.TipoDocumento;
import utilidades.*;
import utils.Tabla;
import utils.TextMenu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static utils.Texto.centrar;

public class UISVP {
    // Atributos
    private static UISVP instance = null;
    private final Scanner sc;

    // Constructor
    private UISVP() {
        sc = new Scanner(System.in);
        sc.useDelimiter("[\\t\\r\\n]+");
        cargarDatos();
    }

    // Metodos
    public static UISVP getInstance() {
        if (instance == null) {
            instance = new UISVP();
        }
        return instance;
    }

    private void cargarDatos() {
        String[] ruts = {"11.111.111-1", "22.222.222-2", "33.333.333-3", "44.444.444-4"};
        Tratamiento[] tratamiento = {Tratamiento.SR, Tratamiento.SRA, Tratamiento.SRA, Tratamiento.SR};
        String[] nombre = {"José Esteban", "María Paz", "Ángela Eugenia", "Carlos Patricio"};
        String[] paterno = {"Cid", "Daza", "Muñoz", "Figueroa"};
        String[] materno = {"Aedo", "Barrera", "Naranjo", "Orellana"};
        String[] email = {"jose@fake.email.cl", "maria@fake.email.cl", "angela@fake.email.cl", "carlos@fake.email.cl"};
        String[] fono = {"(+569) 1122 3344", "(+569) 1672 3421", "(+569) 6172 3323", "(+569) 9872 6472"};

        // Creación de clientes
        for (int i = 0; i < ruts.length; i++) {
            Rut idCli = Rut.of(ruts[i]);
            Nombre nombreCli = new Nombre(tratamiento[i], nombre[i], paterno[i], materno[i]);
            SistemaVentaPasajes.getInstance().createCliente(idCli, nombreCli, fono[i], email[i]);
        }

        // Creación de pasajeros
        for (int i = ruts.length - 1; i >= 0; i--) {
            int j = ruts.length - i - 1;
            Rut idCli = Rut.of(ruts[i]);
            Nombre nombreCli = new Nombre(tratamiento[i], nombre[i], paterno[i], materno[i]);
            Nombre contacto = new Nombre(tratamiento[j], nombre[j], paterno[j], materno[j]);
            SistemaVentaPasajes.getInstance().createPasajero(idCli, nombreCli, fono[i], contacto, fono[j]);
        }

        // Creación de terminales
        String[][] terminales = {
                {"El Pinar", "Los Alamos", "450", "Pinto"},
                {"El Nevado Alto", "Las Violetas", "1830", "Medina"},
                {"Sofia III", "San Jerónimo", "169", "Santa Faz"}
        };
        for (String[] terminal : terminales) {
            ControladorEmpresas.getInstance().createTerminal(terminal[0],
                    new Direccion(terminal[1], Integer.parseInt(terminal[2]), terminal[3]));
        }

        // Creación de empresas
        String[][] empresas = {
                {"88.888.888-8", "Buses Ñuble", "https://www.busesnuble.cl"},
                {"99.999.999-9", "Efe Bus", "https://www.efebus.cl"},
        };
        for (String[] empresa : empresas) {
            ControladorEmpresas.getInstance().createEmpresa(Rut.of(empresa[0]), empresa[1], empresa[2]);
        }

        // Creación de buses
        String[][] buses = {
                {"ABCD-12", "Mercedes-Benz", "O500", "45", empresas[0][0]},
                {"EFGH-34", "Volvo", "B450R", "52", empresas[0][0]},
                {"IJKL-56", "Scania", "K310", "40", empresas[0][0]},
                {"MNOP-78", "Marcopolo", "Paradiso", "38", empresas[0][0]},
                {"QRST-90", "Irizar", "i6", "44", empresas[0][0]}
        };
        for (String[] bus : buses) {
            ControladorEmpresas.getInstance().createBus(bus[0], bus[1], bus[2], Integer.parseInt(bus[3]),
                    Rut.of(bus[4]));
        }

        // Contratación de auxiliares y conductores
        String[][] tripulantes = {
                {"55.555.555-5", "Juan Esteban", "Lagos", "Rios", "San Carlos", "201", "Talcahuano"},
                {"66.666.666-6", "Diego Andrés", "Pavez", "Martel", "Av Francia", "730", "Los Lagos"},
                {"77.777.777-7", "Elena Rosa", "Saez", "Torres", "Andalucia", "2778", "Linares"},
        };

        String[] tripulante;
        for (int i = 0; i < tripulantes.length - 1; i++) {
            tripulante = tripulantes[i];
            ControladorEmpresas.getInstance().hireAuxiliarForEmpresa(Rut.of(empresas[0][0]),
                    Rut.of(tripulante[0]),
                    new Nombre(Tratamiento.SR, tripulante[1], tripulante[2], tripulante[3]),
                    new Direccion(tripulante[4], Integer.parseInt(tripulante[5]), tripulante[6]));
        }
        tripulante = tripulantes[tripulantes.length - 1];
        ControladorEmpresas.getInstance().hireConductorForEmpresa(Rut.of(empresas[0][0]),
                Rut.of(tripulante[0]),
                new Nombre(Tratamiento.SRA, tripulante[1], tripulante[2], tripulante[3]),
                new Direccion(tripulante[4], Integer.parseInt(tripulante[5]), tripulante[6]));

        IdPersona[][] idTripulantes = {
                {Rut.of(tripulantes[0][0]), Rut.of(tripulantes[2][0])},
                {Rut.of(tripulantes[1][0]), Rut.of(tripulantes[2][0])}
        };

        String[][] nomComunas = {
                {"Pinto", "Medina"},
                {"Pinto", "Santa Faz"}
        };

        // Creación de viajes
        LocalDate fecha = LocalDate.now();
        LocalTime hora = LocalTime.of(8, 0);
        int[] precio = {3000, 3500, 4000, 5500, 6000};
        int[] duracion = {60, 80, 90, 110, 120};

        // Viajes para los próximos 5 días
        for (int dia = 0; dia < 5; dia++) {
            fecha = fecha.plusDays(1);
            for (int i = 0; i < 5; i++) {
                SistemaVentaPasajes.getInstance().createViaje(fecha, hora.plusMinutes(i * 75),
                        precio[i % precio.length], duracion[i % duracion.length], buses[i % buses.length][0],
                        idTripulantes[i % idTripulantes.length], nomComunas[i % nomComunas.length]);
            }
        }
    }

    public void menu() {
        TextMenu menuPrincipal = new TextMenu("Menú principal");
        menuPrincipal.setOpciones("Crear empresa", "Contratar tripulante", "Crear terminal",
                "Crear cliente", "Crear bus", "Crear viaje", "Vender pasajes",
                "Listar ventas", "Listar viajes", "Listar pasajeros de viaje",
                "Listar empresas", "Listar llegadas/salidas de terminal",
                "Listar ventas de empresa");
        int opcion;
        do {
            menuPrincipal.desplegar();
            opcion = leePositivo(menuPrincipal.opSalir(), "Opción no válida");
            switch (opcion) {
                case 1 -> createEmpresa();
                case 2 -> contrataTripulante();
                case 3 -> createTerminal();
                case 4 -> createCliente();
                case 5 -> createBus();
                case 6 -> createViaje();
                case 7 -> vendePasajes();
                case 8 -> listVentas();
                case 9 -> listViajes();
                case 10 -> listPasajerosViaje();
                case 11 -> listEmpresas();
                case 12 -> listLlegadasYSalidasDeTerminal();
                case 13 -> listVentasEmpresa();
            }

        } while (opcion != menuPrincipal.opSalir());
        System.out.println(centrar("...:::: Gracias por utilizar el sistema ::::....", menuPrincipal.getAncho()));
    }

    private void createEmpresa() {

        GUICreaEmpresa dialog = new GUICreaEmpresa();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        /*titulo("Creando una nueva Empresa");
        Rut rut = leeRut();
        label("Nombre");
        String nombre = leeStringAlfanumerico();
        label("url");
        String url = leeUrl();
        try {
            ControladorEmpresas.getInstance().createEmpresa(rut, nombre, url);
            titulo("Empresa guardada exitosamente");
        } catch (SistemaVentaPasajesException e) {
            error(e.getMessage());
        }*/
    }

    private void contrataTripulante() {

        GUIContratarTripulante dialog = new GUIContratarTripulante();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        /*titulo("Contatando un nuevo Tripulante");
        subTitulo("Dato de la Empresa");
        Rut rutEmpresa = leeRut();
        subTitulo("Datos tripulante");
        int tipoTripulante = leeTipoTripulante(); // 1: auxiliar, 2: conductor
        IdPersona idPersona = leeIDPersona();
        Nombre nombre = leeNombreCompleto();
        Direccion direccion = leeDireccion();
        try {
            if (tipoTripulante == 1) {
                ControladorEmpresas.getInstance().hireAuxiliarForEmpresa(rutEmpresa, idPersona, nombre, direccion);
                titulo("Auxiliar contratado exitosamente");
            } else {
                ControladorEmpresas.getInstance().hireConductorForEmpresa(rutEmpresa, idPersona, nombre, direccion);
                titulo("Conductor contratado exitosamente");
            }
        } catch (SistemaVentaPasajesException e) {
            error(e.getMessage());
        }*/
    }

    private void createTerminal() {
        titulo("Creando un nuevo Terminal");
        label("Nombre");
        String nombre = leeStringAlfanumerico();
        Direccion direccion = leeDireccion();
        try {
            ControladorEmpresas.getInstance().createTerminal(nombre, direccion);
            titulo("Terminal guardado exitosamente");
        } catch (SVPExepction e) {
            error(e.getMessage());
        }
    }

    private void createCliente() {
        titulo("Creando un nuevo Cliente");
        IdPersona id = leeIDPersona();
        Nombre nombreCompleto = leeNombreCompleto();
        label("Teléfono móvil");
        String telefono = leeTelefono();
        label("Email");
        String email = leeEmail();
        try {
            SistemaVentaPasajes.getInstance().createCliente(id, nombreCompleto, telefono, email);
            titulo("Cliente guardado exitosamente");
        } catch (SVPExepction e) {
            error(e.getMessage());
        }
    }

    private void createBus() {
        GUICreacionDeBus dialog = new GUICreacionDeBus();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        /*titulo("Creando un nuevo Bus");
        label("Patente");
        String patente = leePatente();
        label("Marca");
        String marca = leeStringAlfanumerico();
        label("Modelo");
        String modelo = leeStringAlfanumerico();
        label("Número de asientos");
        int nro = leePositivo(70, "Nro. de asientos no válido");
        subTitulo("Dato de la empresa");
        Rut rutEmpresa = leeRut();
        try {
            ControladorEmpresas.getInstance().createBus(patente, marca, modelo, nro, rutEmpresa);
            titulo("Bus guardado exitosamente");
        } catch (SistemaVentaPasajesException e) {
            error(e.getMessage());
        }*/
    }

    private void createViaje() {
        titulo("Creando un nuevo Viaje");
        label("Fecha[dd/mm/yyyy]");
        LocalDate fecha = leeFecha("Fecha de viaje no válida");
        label("Hora[hh:mm]");
        LocalTime hora = leeHora("Hora de viaje no válida");
        label("Precio");
        int precio = leePositivo("El número debe ser positivo");
        label("Duración (minutos)");
        int duracion = leePositivo("El número debe ser positivo");
        label("Patente Bus");
        String patBus = sc.next();
        label("Nro. de conductores");
        int nroConductores = leePositivo(2, "Valor debe ser positivo menor a 2");
        IdPersona[] idPersonas = new IdPersona[nroConductores + 1];
        System.out.printf("%25s%n", ":: Id Auxiliar ::");
        idPersonas[0] = leeIDPersona();
        System.out.printf("%25s%n", ":: Id Conductor ::");
        idPersonas[1] = leeIDPersona();
        if (nroConductores > 1) {
            System.out.printf("%25s", ":: Id Conductor 2 ::");
            idPersonas[2] = leeIDPersona();
        }
        String[] nomComunas = new String[2];
        label("Nombre comuna salida");
        nomComunas[0] = leeStringAlfabetico();
        label("Nombre comuna llegada");
        nomComunas[1] = leeStringAlfabetico();
        try {
            SistemaVentaPasajes.getInstance().createViaje(fecha, hora, precio, duracion, patBus,
                    idPersonas, nomComunas);
            titulo("Viaje guardado exitosamente");
        } catch (SVPExepction e) {
            error(e.getMessage());
        }
    }

    private void vendePasajes() {
        titulo("Venta de pasajes");
        // Inicia venta
        subTitulo("Datos de la Venta");
        label("ID Documento");
        String idDoc = leeStringAlfanumerico();
        label("Tipo documento: [1] Boleta [2] Factura");
        int op = leePositivo(2, "Opción no válida");
        TipoDocumento tipoDocumento = switch (op) {
            case 1 -> TipoDocumento.BOLETA;
            case 2 -> TipoDocumento.FACTURA;
            default -> throw new IllegalStateException("Valor inesperado: " + op);
        };
        label("Fecha de viaje[dd/mm/yyyy]");
        LocalDate fechaViaje = leeFecha("Fecha de viaje no válida");
        label("Origen (comuna)");
        String comunaSalida = leeStringAlfabetico();
        label("Destino (comuna)");
        String comunaLlegada = leeStringAlfabetico();
        subTitulo("Datos del cliente");
        System.out.println();
        IdPersona idCliente = leeIDPersona();
        try {
            // Venta de pasajes individuales
            subTitulo("Pasajes a vender");
            label("Cantidad de pasajes");
            int cantidadPasajesPorVender = leeMayorQue(0);
            SistemaVentaPasajes.getInstance().iniciaVenta(idDoc, tipoDocumento, fechaViaje,
                    comunaSalida, comunaLlegada, idCliente, cantidadPasajesPorVender);
            int pasajesYaVendidos = 0;
            while (pasajesYaVendidos < cantidadPasajesPorVender) {
                String[] datosViaje = getDatosViajeSeleccionado(fechaViaje, comunaSalida, comunaLlegada,
                        cantidadPasajesPorVender - pasajesYaVendidos);

                subTitulo("Asientos disponibles para el viaje seleccionado");
                String patente = datosViaje[0];
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime hora = LocalTime.parse(datosViaje[1], dtf);
                String[] asientosSeleccionados = getAsientosSeleccionados(fechaViaje, hora, patente,
                        cantidadPasajesPorVender, pasajesYaVendidos);

                // Crear pasajes, validando el asiento seleccionado
                for (String asientoVendido : asientosSeleccionados) {
                    subTitulo("Datos pasajeros " + (pasajesYaVendidos + 1));
                    IdPersona idPasajero = leeIDPersona();

                    creaPasajeroSiNoExiste(idPasajero);
                    try {
                        SistemaVentaPasajes.getInstance()
                                .vendePasaje(idDoc, tipoDocumento, fechaViaje, hora, patente,
                                        Integer.parseInt(asientoVendido.trim()), idPasajero);
                        subTitulo("Pasaje agregado exitosamente");
                        pasajesYaVendidos++;
                    } catch (SVPExepction e) {
                        error("Pasaje no se pudo generar");
                    }
                }
            }
            Optional<Integer> montoOptional = SistemaVentaPasajes.getInstance()
                    .getMontoVenta(idDoc, tipoDocumento);
            if (montoOptional.isPresent()) {
                int monto = montoOptional.get();
                subTitulo(String.format("Monto total de la venta: $%,d", +monto));
                pagaVentaPasajes(idDoc, tipoDocumento);
                titulo("Venta realizada exitosamente");
            } else {
                error("No es posible recuperar monto de la venta");
            }
        } catch (SVPExepction e) {
            error(e.getMessage());
        }
    }

    private String[] getDatosViajeSeleccionado(LocalDate fechaViaje, String comunaSalida,
                                               String comunaLlegada, int nroAsientos) {
        // Iniciar metodo privado
        subTitulo("Listado de horarios disponibles");
        String[] column = {"BUS", "SALIDA", "VALOR", "ASIENTOS"};
        int[] len = {8, 8, 8, 8};
        char[] align = {'l', 'r', 'r', 'r'};
        String[][] data = SistemaVentaPasajes.getInstance()
                .getHorariosDisponibles(fechaViaje, comunaSalida, comunaLlegada, nroAsientos);
        // Se sabe que hay horarios disponibles para la fecha de viaje y el origen y destino ingresados
        // porque se validó al iniciar la venta
        Tabla.printTableNumbers(data, column, len, align, 0);
        label("Seleccione viaje [1.." + data.length + "]");
        int seleccion = leePositivo(data.length, "Viaje no válido");
        String[] datosViaje = new String[2];
        datosViaje[0] = data[seleccion - 1][0];
        datosViaje[1] = data[seleccion - 1][1];
        return datosViaje;
    }

    private String[] getAsientosSeleccionados(LocalDate fechaViaje, LocalTime hora, String patente,
                                              int cantPasajesRequeridos, int pasajesVendidos) {
        // Se asume en esta versión un layout de 4 columnas de asientos (impares ventana)
        String[] asientosDisponibles = SistemaVentaPasajes.getInstance().listAsientosDeViaje(fechaViaje, hora, patente);
        String[][] asientosBus = new String[(asientosDisponibles.length + 3) / 4][5];
        int iAsiento = 0;
        for (int i = 0; i < asientosBus.length; i++) {
            for (int j = 0; j < asientosBus[0].length; j++) {
                if (j == 2) {
                    // Pasillo!!!
                    asientosBus[i][j] = " ";
                } else {
                    asientosBus[i][j] = asientosDisponibles[iAsiento];
                    iAsiento++;
                }
            }
        }

        // Reordenar columnas 3 y 4
        for (int i = 0; i < asientosBus.length; i++) {
            String aux = asientosBus[i][3];
            asientosBus[i][3] = asientosBus[i][4];
            asientosBus[i][4] = aux;
        }

        // Imprimir tabla de asientos
        int[] len2 = {1, 1, 1, 1, 1};
        char[] align2 = {'l', 'l', 'l', 'l', 'l'};
        Tabla.printTable(asientosBus, len2, align2, 0);
        label("Seleccione sus asientos [separe por ,]");
        String[] asientosSel = sc.next().trim().split(",");
        while (asientosSel.length < 1 || asientosSel.length > (cantPasajesRequeridos - pasajesVendidos)) {
            error("No puede seleccionar más de [" + (cantPasajesRequeridos - pasajesVendidos) + "]");
            System.out.print("..:: Intente de nuevo: ");
            asientosSel = sc.next().trim().split(",");
        }
        return asientosSel;
    }

    private void creaPasajeroSiNoExiste(IdPersona idPasajero) {
        Optional<String> nombreOptional = SistemaVentaPasajes.getInstance().getNombrePasajero(idPasajero);
        if (nombreOptional.isEmpty()) {
            System.out.print("..:: Ingreso de nuevo pasajero: ");
            subTitulo("Nombre del pasajero");
            Nombre nombre = leeNombreCompleto();
            label("Teléfono Pasajero");
            String fono = leeTelefono();
            subTitulo("Contacto");
            Nombre contacto = leeNombreCompleto();
            label("Teléfono de contacto");
            String fonoContacto = leeTelefono();
            SistemaVentaPasajes.getInstance().createPasajero(idPasajero, nombre, fono, contacto, fonoContacto);
        }
    }

    private void pagaVentaPasajes(String idDoc, TipoDocumento tipoDocumento) {
        subTitulo("Pago de la venta");
        int tipoPago = leeTipoPago(); // 1: efectivo, 2: tarjeta
        if (tipoPago == 1) {
            SistemaVentaPasajes.getInstance().pagaVenta(idDoc, tipoDocumento);
        } else {
            label("Nro. tarjeta");
            long nroTarjeta = leeLongPositivo("Numero no válido");
            SistemaVentaPasajes.getInstance().pagaVenta(idDoc, tipoDocumento, nroTarjeta);
        }
    }

    private void listVentas() {
        titulo("Listado de ventas");
        String[][] data = SistemaVentaPasajes.getInstance().listVentas();
        if (data.length == 0) {
            data = new String[][]{{"-", "-", "-", "-", "-", "-", "-"}};
        }
        String[] column = {"ID DOCUMENTO", "TIPO DOCUMENTO", "FECHA", "RUT/PASAPORTE", "CLIENTE", "CANT BOLETOS", "TOTAL VENTA"};
        int[] len = {10, 8, 10, 15, 30, 12, 12};
        char[] align = {'r', 'l', 'r', 'r', 'l', 'r', 'r'};
        Tabla.printTable(data, column, len, align, 0);
    }

    private void listViajes() {
        //Tabla.setTopDoubleStyle();
        titulo("Listado de viajes");
        String[][] data = SistemaVentaPasajes.getInstance().listViajes();
        //{"10/10/2024", "8:10", "$3.000", "12", "BB.CC-45" "Chillan" "Concepcion"}
        String[] column = {"FECHA", "HORA SALE", "HORA LLEGA", "PRECIO", "ASIENTOS DISP.",
                "PATENTE", "ORIGEN", "DESTINO"};
        int[] len = {12, 11, 12, 6, 14, 12, 15, 15};
        char[] align = {'l', 'r', 'r', 'r', 'r', 'l', 'l', 'l'};
        Tabla.printTable(data, column, len, align, 0);
    }

    private void listPasajerosViaje() {
        titulo("Listado de pasajeros de un viaje");
        label("Fecha del viaje[dd/mm/yyyy]");
        LocalDate fecha = leeFecha("Fecha de viaje no válida");
        label("Hora del viaje[hh:mm]");
        LocalTime hora = leeHora("Hora no válida");
        label("Patente bus");
        String patBus = leeStringAlfanumerico();
        try {
            String[][] data = SistemaVentaPasajes.getInstance().listPasajerosViaje(fecha, hora, patBus);
            if (data.length == 0) {
                titulo("No hay pasajeros asociados al viaje solicitado");
            } else {
                String[] column = {"ASIENTO", "RUT/PASS", "PASAJERO", "CONTACTO", "TELEFONO CONTACTO"};
                int[] len = {7, 15, 30, 30, 17};
                char[] align = {'r', 'r', 'l', 'l', 'l'};
                Tabla.printTable(data, column, len, align, 0);
            }
        } catch (SVPExepction e) {
            error(e.getMessage());
        }
    }

    private void listEmpresas() {
        titulo("Listado de empresas");
        String[][] data = ControladorEmpresas.getInstance().listEmpresas();
        if (data.length == 0) {
            data = new String[][]{{"-", "-", "-", "-", "-", "-"}};
        }
        String[] column = {"RUT EMPRESA", "NOMBRE", "URL", "NRO. TRIPULANTES", "NRO. BUSES", "NRO. VENTAS"};
        int[] len = {12, 30, 30, 16, 10, 11};
        char[] align = {'l', 'l', 'l', 'r', 'r', 'r'};
        Tabla.printTable(data, column, len, align, 0);
    }

    private void listLlegadasYSalidasDeTerminal() {
        titulo("Listado de llegadas y salidas de un terminal");
        label("Nombre terminal");
        String nombre = leeStringAlfanumerico();
        label("Fecha[dd/mm/yyyy]");
        LocalDate fecha = leeFecha("Fecha no válida");
        try {
            String[][] data = ControladorEmpresas.getInstance().listLlegadasSalidasTerminal(nombre, fecha);
            if (data.length == 0) {
                titulo("No hay viajes programados para la fecha dada en el terminal solicitado");
            } else {
                String[] column = {"LLEGADA/SALIDA", "HORA", "PATENTE BUS", "NOMBRE EMPRESA", "NRO. PASAJEROS"};
                int[] len = {14, 5, 11, 25, 14};
                char[] align = {'l', 'l', 'l', 'l', 'r'};
                Tabla.printTable(data, column, len, align, 0);
            }
        } catch (SVPExepction e) {
            error(e.getMessage());
        }
    }

    private void listVentasEmpresa() {
        titulo("Listado de ventas de una empresa");
        Rut rut = leeRut();
        try {
            String[][] data = ControladorEmpresas.getInstance().listVentasEmpresa(rut);
            if (data.length == 0) {
                titulo("No hay ventas asociadas a la empresa solicitada");
            } else {
                String[] column = {"FECHA", "TIPO", "MONTO PAGADO", "TIPO PAGO"};
                int[] len = {12, 7, 12, 13};
                char[] align = {'l', 'l', 'l', 'r', 'l'};
                Tabla.printTable(data, column, len, align, 0);
            }
        } catch (SVPExepction e) {
            error(e.getMessage());
        }
    }

    private String leeUrl() {
        String url = sc.next().trim();
        if (!url.matches("^(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*"
                + "(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?$")) {
            error("Dirección url no es válida");
            System.out.print("..:: intente de nuevo: ");
            url = sc.next().trim();
        }
        return url;
    }

    private String leePatente() {
        String patente = sc.next().trim();
        if (!patente.matches("^([a-zA-Z]{4})([\\-| ]?)([0-9]{2})?$")) {
            error("Patente no es válida");
            System.out.print("..:: intente de nuevo: ");
            patente = sc.next().trim();
        }
        return patente;
    }

    private IdPersona leeIDPersona() {
        label("Rut[1] o Pasaporte[2]");
        int op = leePositivo(2, "Opción no válida");
        IdPersona id;
        switch (op) {
            case 1 -> id = leeRut();
            case 2 -> id = leePasaporte();
            default -> throw new IllegalStateException("Valor inesperado: " + op);
        }
        return id;
    }

    private Nombre leeNombreCompleto() {
        int op;
        //leer nombre completo
        label("Sr.[1] o Sra.[2]");
        op = leePositivo(2, "Opción no válida");
        Tratamiento tr = switch (op) {
            case 1 -> Tratamiento.SR;
            case 2 -> Tratamiento.SRA;
            default -> Tratamiento.SR;
        };
        label("Nombres");
        String nombre = leeStringAlfabetico();
        label("Apellido Paterno");
        String apPaterno = leeStringAlfabetico();
        label("Apellido Materno");
        String apMaterno = leeStringAlfabetico();
        return new Nombre(tr, nombre, apPaterno, apMaterno);
    }

    private String leeEmail() {
        String email = sc.next().trim();
        if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            error("Dirección de email no válida");
            System.out.print("..:: intente de nuevo: ");
            email = sc.next().trim();
        }
        return email;
    }

    private String leeTelefono() {
        String telefono = sc.next().trim();
        if (!telefono.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")) {
            error("Número de teléfono no válido");
            System.out.print("..:: intente de nuevo: ");
            telefono = sc.next().trim();
        }
        return telefono;
    }

    private String leeStringNoVacio() {
        String str = sc.next().trim();
        while (str.isEmpty()) {
            error("No debe ser vacío o incluir solo espacios");
            System.out.print("..:: intente de nuevo: ");
            str = sc.next().trim();
        }
        return str;
    }

    private String leeStringAlfanumerico() {
        String str = sc.next().trim();
        while (!str.matches("^[a-zA-Z0-9 ]+$")) {
            error("Solo debe incluir caracteres alfanuméricos");
            System.out.print("..:: intente de nuevo: ");
            str = sc.next().trim();
        }
        return str;
    }

    private String leeStringAlfabetico() {
        String str = sc.next().trim();
        while (!str.matches("^[a-zA-Z ]+$")) {
            error("Solo debe incluir caracteres alfabéticos");
            System.out.print("..:: intente de nuevo: ");
            str = sc.next().trim();
        }
        return str;
    }

    private int leeTipoTripulante() {
        label("Auxiliar[1] o Conductor[2]");
        return leePositivo(2, "Tipo de tripulante no válido");
    }

    private int leeTipoPago() {
        label("Efectivo[1] o Tarjeta[2]");
        return leePositivo(2, "Tipo de pago no válido");
    }

    private LocalTime leeHora() {
        try {
            String[] time = sc.next().split(":");
            return LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        } catch (Exception e) {
            return null;
        }
    }

    private LocalTime leeHora(String error) {
        LocalTime hora = leeHora();
        while (hora == null) {
            error(error);
            System.out.print("..:: intente de nuevo [hh:mm]: ");
            hora = leeHora();
        }
        return hora;
    }

    private LocalDate leeFecha() {
        try {
            String[] fecha = sc.next().split("/");
            return LocalDate.of(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate leeFecha(String error) {
        LocalDate fecha = leeFecha();
        while (fecha == null) {
            error(error);
            System.out.print("..:: intente de nuevo [dd/mm/yyyy]: ");
            fecha = leeFecha();
        }
        return fecha;
    }

    private int leePositivo(String error) {
        return leePositivo(Integer.MAX_VALUE, error);
    }

    private int leeMayorQue(int min) {
        return leeEnRango(min, Integer.MAX_VALUE, "El número debe ser mayor que " + min);
    }

    private int leePositivo(int max, String error) {
        return leeEnRango(1, max, error);
    }

    private Long leeLongPositivo(String error) {
        long lnum;
        String snum = sc.next().trim();
        do {
            while (!(snum.matches("^[-+]?\\d+$"))) {
                error(error);
                System.out.print("..:: intente de nuevo: ");
                snum = sc.next().trim();
            }
            lnum = Long.parseLong(snum);
            if (lnum <= 0) {
                error("Valor debe ser mayor a cero");
            }
        } while (lnum <= 0);
        return lnum;
    }

    private int leeEnRango(int min, int max, String error) {
        int i = leeEntero(error);
        while (i < min || i > max) {
            error(error);
            System.out.print("..:: intente de nuevo: ");
            i = sc.nextInt();
        }
        return i;
    }

    private int leeEntero(String error) {
        String snum = sc.next().trim();
        while (!(snum.matches("^[-+]?\\d+$"))) {
            error(error);
            System.out.print("..:: intente de nuevo: ");
            snum = sc.next().trim();
        }
        return Integer.parseInt(snum);
    }

    private Rut leeRut() {
        label("R.U.T");
        Rut rut = Rut.of(sc.next());
        while (rut == null) {
            error("R.U.T no válido");
            System.out.print("..:: intente de nuevo: ");
            rut = Rut.of(sc.next());
        }
        return rut;
    }

    private IdPersona leePasaporte() {
        label("Numero Pasaporte");
        String numero = leeStringAlfanumerico();
        label("Nacionalidad");
        String nacionalidad = leeStringAlfabetico();
        Pasaporte pasaporte = Pasaporte.of(numero, nacionalidad);
        while (pasaporte == null) {
            error("Pasaporte no válido");
            label("Numero Pasaporte");
            numero = leeStringAlfanumerico();
            label("Nacionalidad");
            nacionalidad = leeStringAlfabetico();
            pasaporte = Pasaporte.of(numero, nacionalidad);
        }
        return pasaporte;
    }

    private Direccion leeDireccion() {
        label("Calle");
        String calle = leeStringAlfanumerico();
        label("Numero");
        int numero = leeMayorQue(0);
        label("Comuna");
        String comuna = leeStringAlfabetico();
        return new Direccion(calle, numero, comuna);
    }

    private void error(String s) {
        System.out.printf("*** Error: %s ***%n", s);
    }

    private void label(String s) {
        System.out.printf("%25s : ", s);
    }

    private void titulo(String s) {
        System.out.println();
        System.out.println(centrar(String.format("...:::: %s ::::....", s), 50));
        System.out.println();
    }

    private void subTitulo(String s) {
        System.out.println();
        System.out.printf(":::: %s %n", s);
    }
}
