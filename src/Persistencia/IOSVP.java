package Persistencia;

import excepciones.SVPExepction;
import modelo.*;
import utilidades.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IOSVP {

    private static IOSVP instance = null;
    public static IOSVP getInstance() {
        if (instance == null) {
            instance = new IOSVP();
        }
        return instance;
    }

    private static final Scanner scr = new Scanner(System.in);

    private IOSVP() {}


    public Object[] readDatosIniciales() throws SVPExepction {
        File datosIniciales = new File("src/SVPDatosIniciales");
        Scanner scr;

        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Pasajero> pasajeros = new ArrayList<>();
        ArrayList<Empresa> empresas = new ArrayList<>();
        ArrayList<Tripulante> tripulantes = new ArrayList<>();
        ArrayList<Auxiliar> auxiliares = new ArrayList<>();
        ArrayList<Conductor> conductores = new ArrayList<>();
        ArrayList<Terminal> terminales = new ArrayList<>();
        ArrayList<Bus> buses = new ArrayList<>();
        ArrayList<Viaje> viajes = new ArrayList<>();

        try {
            scr = new Scanner(new FileInputStream(datosIniciales));

            int sec = 0;
            while (scr.hasNextLine()) {
                String linea = scr.nextLine().trim();
                if (linea.equals("+")) {
                    if (scr.hasNextLine()) {
                        sec += 1;
                    }
                } else if (sec < 6) {
                    switch (sec) {
                        case 0 -> consSec1(linea, clientes, pasajeros);
                        case 1 -> consSec2(linea, empresas);
                        case 2 -> consSec3(linea, empresas, auxiliares, conductores, tripulantes);
                        case 3 -> consSec4(linea, terminales);
                        case 4 -> consSec5(linea, empresas, buses);
                        case 5 -> consSec6(linea, viajes, buses, terminales);
                    }
                }
            }
        } catch (SVPExepction | FileNotFoundException e) {
            throw new RuntimeException("No existe o no fue posible abrir el archivo");
        }
        return new Object[]{clientes, pasajeros, empresas, tripulantes, auxiliares, conductores, terminales, buses, viajes};
    }

    private static LocalDate constructorFecha(String fecha) {
        return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private static LocalTime constructorHora(String hora) {
        LocalTime time;
        String [] horaArray = hora.split(":");
        time = LocalTime.of(Integer.parseInt(horaArray[0]), Integer.parseInt(horaArray[1]));
        return time;
    }


    private void consSec1(String linea, ArrayList<Cliente> clientes, ArrayList<Pasajero> pasajeros) {
        String [] split = linea.split(",");
        String tipo = split[0];
        Tratamiento tratamiento = Tratamiento.valueOf(split[2]);
        Nombre nombre = new Nombre(tratamiento, split[3], split[4], split[5]);
        Rut rut = Rut.of(split[1]);
        String email = split[7];
        String telefono = split[6];

        switch (tipo) {
            case "C" ->{
                Cliente cliente = new Cliente(rut, nombre, telefono, email);
                clientes.add(cliente);
            }
            case "P" ->{
                String fonoContacto = split[11];
                Tratamiento tratContacto = null;
                if(split[8].equals("SR")){
                    tratContacto = Tratamiento.SR;
                } else if(split[8].equals("SRA")){
                    tratContacto = Tratamiento.SRA;
                }
                Nombre nomContacto = new Nombre(tratContacto, split[9], split[10], split[11]);
                Pasajero pasajero = new Pasajero(rut, nombre, telefono, nomContacto, fonoContacto);
                pasajeros.add(pasajero);
            }
            case "CP" ->{
                String fonoContacto = split[12];
                Tratamiento tratContacto = null;
                if(split[8].equals("SRA")){
                    tratContacto = Tratamiento.SRA;
                }
                Nombre nomContacto = new Nombre(tratContacto, split[9], split[10], split[11]);
                Cliente cliente = new Cliente(rut, nombre, telefono, email);
                clientes.add(cliente);
                Pasajero pasajero = new Pasajero(rut, nombre, telefono, nomContacto, fonoContacto);
                pasajeros.add(pasajero);
            }
        }
    }

    private void consSec2(String linea, ArrayList<Empresa> empresas) {
        String [] split = linea.split(",");
        Rut rut = Rut.of(split[0]);
        String nombre = split[1];
        String url = split[2];
        Empresa empresa = new Empresa(rut, nombre);
        empresa.setUrl(url);
        empresas.add(empresa);
    }

    private void consSec3(String linea, ArrayList<Empresa> empresas, ArrayList<Auxiliar> auxiliares, ArrayList<Conductor>conductores, ArrayList<Tripulante>tripulantes) {
        String [] split = linea.split(",");
        String tipo = split[0];
        Rut rut = Rut.of(split[1]);
        Tratamiento tratamiento = Tratamiento.valueOf(split[2]);
        Nombre nombre = new Nombre(tratamiento, split[3], split[4], split[5]);
        Direccion direccion = new Direccion(split[6], Integer.parseInt(split[7]), split[8]);
        Rut rutEmpresa = Rut.of(split[9]);
        Optional<Empresa> empresaOptional = IOSVP.instance.findEmpresa(empresas, rutEmpresa);

        if(empresaOptional.isPresent()){
            Empresa empresa = empresaOptional.get();
            switch(tipo){
                case "A" ->{
                    Auxiliar aux = new Auxiliar(rutEmpresa, nombre, direccion);
                    auxiliares.add(aux);
                    tripulantes.add(aux);
                }
                case "C" ->{
                    Conductor conductor = new Conductor(rutEmpresa, nombre, direccion);
                    conductores.add(conductor);
                    tripulantes.add(conductor);
                }
            }
        }
    }

    private void consSec4(String linea, ArrayList<Terminal> terminales) {
        String [] split = linea.split(",");
        String nombre = split[0];
        Direccion direccion = new Direccion(split[1], Integer.parseInt(split[2]), split[3]);
        terminales.add(new Terminal(nombre, direccion));
    }

    private void consSec5(String linea, ArrayList<Empresa> empresas, ArrayList<Bus> buses) {
        String [] split = linea.split(",");
        String patente = split[0];
        String marca = split[1];
        String modelo = split[2];

        int nroAsientos = Integer.parseInt(split[3]);

        Rut rut = Rut.of(split[4]);
        Optional<Empresa> empresaOptional = IOSVP.getInstance().findEmpresa(empresas, rut);

        if(empresaOptional.isPresent()){
            Bus bus = new Bus(patente, nroAsientos, empresaOptional.get());
            bus.setMarca(marca);
            bus.setModelo(modelo);
            buses.add(bus);
        }
    }

    private void consSec6(String linea, ArrayList<Viaje> viajes, ArrayList<Bus> buses, ArrayList<Terminal> terminales) {
        String [] split = linea.split(",");
        LocalDate fecha = constructorFecha(split[0]);
        LocalTime hora = constructorHora(split[1]);
        int precio = Integer.parseInt(split[2]);
        int duracion = Integer.parseInt(split[3]);
        String patente = split[4];
        Rut rutAux = Rut.of(split[5]);
        Rut rutCon = Rut.of(split[6]);


        String nomTermSalida = split[7];
        String nomTermLlegada = split[8];

        Optional<Bus> busOptional = IOSVP.getInstance().findBus(buses, patente);

        if(busOptional.isPresent()){
            Bus bus = busOptional.get();
            Empresa empresa = bus.getEmpresa();
            Optional<Tripulante> auxOptional = IOSVP.getInstance().findTripulante(empresa, rutAux);
            Optional<Tripulante> conOptional = IOSVP.getInstance().findTripulante(empresa, rutCon);

            if(auxOptional.isPresent() && conOptional.isPresent())  {
                Auxiliar auxiliar = (Auxiliar) auxOptional.get();
                Conductor conductor = (Conductor) conOptional.get();
                Conductor [] conductores = {conductor};

                Optional<Terminal> terminalSalida = IOSVP.getInstance().findTerminal(terminales, nomTermSalida);
                Optional<Terminal> terminalLlegada = IOSVP.getInstance().findTerminal(terminales, nomTermLlegada);

                if(terminalSalida.isPresent() && terminalLlegada.isPresent()){
                    Terminal salida = terminalSalida.get();
                    Terminal llegada = terminalLlegada.get();
                    String comunaSalida = terminalSalida.get().getDireccion().getComuna();
                    String comunaLlegada = terminalSalida.get().getDireccion().getComuna();
                    String [] comunas ={comunaSalida,comunaLlegada};
                    Viaje viaje = new Viaje(fecha, hora, precio, duracion, bus, auxiliar, conductor, salida, llegada);
                    viajes.add(viaje);
                }
            }
        }
    }

    private Optional<Empresa>findEmpresa(ArrayList<Empresa> empresas, Rut rut) {
        return empresas.stream()
                .filter(empresa -> empresa.getRut().equals(rut))
                .findFirst();
    }

    private Optional<Tripulante> findTripulante(Empresa empresa, IdPersona idPersona) {
        return Arrays.stream(empresa.getTripulantes())
                .filter(tripulante -> tripulante.getId().equals(idPersona))
                .findFirst();
    }

    private Optional<Bus> findBus(ArrayList<Bus> buses, String patente) {
        return buses.stream()
                .filter(bus -> bus.getPatente().equals(patente))
                .findFirst();
    }

    private Optional<Terminal> findTerminal(ArrayList<Terminal> terminales, String nombre) {
        return terminales.stream()
                .filter(terminal -> terminal.getNombre().equals(nombre))
                .findFirst();
    }


    public Object[] readControladores() throws SVPExepction {

        List<Object> controladores = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SVPObjetos.txt"))) {
            Object [] objLeidos = (Object[]) ois.readObject();
            for (Object o : objLeidos){
                controladores.add(o);
            }
        } catch (ClassNotFoundException e) {
            throw new SVPExepction(("No existe o no se puede abrir el archivo SVPDatosIniciales.obj"));
        } catch (IOException e) {
            throw new SVPExepction(("No se puede leer el archivo SVPObjetos.obj"));
        }
        return controladores.toArray();
    }

    public void saveControladores(Object[] controladores) throws SVPExepction {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SVPObjetos.obj"))) {
            oos.writeObject(controladores);
        }catch (FileNotFoundException e){
            throw new SVPExepction(("No se puede abrir o crear el archivo SVPObjetos.obj"));
        } catch (IOException e){
            throw new SVPExepction("No se puede grabar en el archivo SVPObjetos.obj");
        }
    }

    public void savePasajesDeVenta(List<Pasaje> pasajes, String nombreArchivo) throws SVPExepction {
        File archivosPasaje = new File("src/SVPDatosIniciales.obj");

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivosPasaje))) {
            oos.writeObject(pasajes);
        }catch (FileNotFoundException e){
            throw new SVPExepction("No se puede abrir o crear el archivo " + nombreArchivo);
        } catch (IOException e){
            throw new SVPExepction("No se puede grabar en el archivo: " + nombreArchivo);
        }
    }
}

