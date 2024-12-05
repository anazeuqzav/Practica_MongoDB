package org.app;

import org.dao.ProgramaDAOMongoDB;
import org.modelo.Audiencia;
import org.modelo.Colaborador;
import org.modelo.Horario;
import org.modelo.Programa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProgramaDAOMongoDB programaDAO = new ProgramaDAOMongoDB(); // Suponiendo que tienes esta implementación
        int opcion;

        do {
            System.out.println("""
                    \n
                    ╔═══════════════════════════════════════════════════════════╗
                    ║                 GESTIÓN DE PROGRAMAS DE TV                ║
                    ╠═══════════════════════════════════════════════════════════╣
                    ║ Seleccione una opción de las siguientes:                  ║
                    ║                                                           ║
                    ║   1. Crear un nuevo programa.                             ║
                    ║   2. Listar todos los programas.                          ║
                    ║   3. Consultar un programa por nombre o ID.               ║
                    ║   4. Actualizar un programa.                              ║
                    ║   5. Eliminar un programa.                                ║
                    ║   6. Listar programas de una categoría específica.        ║
                    ║   7. Obtener el programa con mayor audiencia en una fecha.║
                    ║   8. Calcular la audiencia media de un programa en un     ║
                    ║      rango de fechas.                                     ║
                    ║   9. Salir.                                               ║
                    ╚═══════════════════════════════════════════════════════════╝
                    """);
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    crearPrograma(sc, programaDAO);
                    break;
                case 2:
                    List<Programa> programas = programaDAO.buscarTodosProgramas();
                    System.out.println(programas);
                    break;
                case 3:
                    consultarPrograma(sc, programaDAO);
                    break;
                case 4:
                    actualizarPrograma(sc, programaDAO);
                    break;
                case 5:
                    System.out.print("Ingrese el ID del programa a eliminar: ");
                    String id = sc.nextLine();

                    try{
                        programaDAO.eliminarPrograma(id);
                    } catch (IllegalArgumentException e){
                        System.err.println("Formato de ID no válido");
                    }
                    break;
                case 6:
                    System.out.print("Ingrese la categoría: ");
                    String categoria = sc.nextLine();
                    programas = programaDAO.buscarPorProgramaCategoria(categoria);
                    System.out.println(programas);
                    break;

                case 7:
                    Boolean fechaValida = false;
                    while (!fechaValida){
                        try {
                            System.out.println("Ingrese la fecha (yyyy-mm-dd): ");
                            LocalDate fecha = LocalDate.parse(sc.nextLine());
                            fechaValida = true;
                            Programa mayorAudiencia = programaDAO.programaConMayorAudiencia(fecha);
                            if (mayorAudiencia != null) {
                                System.out.println("Programa con mayor audiencia: \n" + mayorAudiencia);
                            } else {
                                System.out.println("No se encontró un programa con audiencia en la fecha proporcionada.");
                            }
                        } catch (DateTimeParseException e){
                            System.err.println("Formato de fecha incorrecto. Vuelve a introducirlo.");
                        }
                    }
                    break;

                case 8:
                    calcularMediaAudienciaDeUnPrograma(sc, programaDAO);
                    break;
                case 9:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Ingrese una opción válida.");
            }
        } while (opcion != 9);

        sc.close();
    }

    // Métodos privados utilizados en el menú

    /**
     * Método para crear un programa. Realiza las peticiones de datos necesarias para completar los datos del programa
     * que se desea ingresar.
     * @param sc Scanner para las peticiones de datos
     * @param programaDAO Clase DAO para realizar la inserccion en mongoDB
     */
    private static void crearPrograma(Scanner sc, ProgramaDAOMongoDB programaDAO) {
        System.out.print("Introduzca el nombre del programa: ");
        String nombre = sc.nextLine();

        System.out.print("Introduzca la categoría a la que pertenece el programa: ");
        String categoria = sc.nextLine();

        System.out.print("Introduzca el día de emisión del programa: ");
        String dia = sc.nextLine();

        LocalTime hora = null;
        boolean horaValida = false;
        while (!horaValida) {
            try {
                System.out.print("Introduzca el horario del programa: (hh:mm): ");
                String horaStr = sc.nextLine();
                hora = LocalTime.parse(horaStr);  // Convierte el String a LocalTime
                horaValida = true;
            } catch (DateTimeParseException e) {
                System.err.println("Formato de hora incorrecto.");
            }
        }

        Horario horario = new Horario(dia, hora);

        // Agregar audiencias
        List<Audiencia> audiencias = new ArrayList<>();
        String continuarAudiencias = "";
        do {
            LocalDate fecha = null;
            boolean fechaValida = false;
            // bucle para comprobar que la fecha que se introduce es válida
            while (!fechaValida) {
                try {
                    System.out.println("Ingrese la fecha de la audiencia (yyyy-mm-dd): ");
                    fecha = LocalDate.parse(sc.nextLine());
                    fechaValida = true;

                } catch (DateTimeParseException e) {
                    System.err.println("Formato de fecha incorrecto.");
                }
            }

            System.out.print("Ingrese el número de espectadores: ");
            int espectadores = sc.nextInt();
            sc.nextLine();

            audiencias.add(new Audiencia(fecha, espectadores));

            System.out.print("¿Desea agregar otra audiencia? (s/n): ");
            continuarAudiencias = sc.nextLine();

        } while (continuarAudiencias.equalsIgnoreCase("s"));

        // Agregar colaboradores
        List<Colaborador> colaboradores = new ArrayList<>();
        String continuarColaboradores;
        do {
            System.out.print("Ingrese el nombre del colaborador: ");
            String nombreColaborador = sc.nextLine();

            System.out.print("Ingrese el rol del colaborador: ");
            String rolColaborador = sc.nextLine();

            colaboradores.add(new Colaborador(nombreColaborador, rolColaborador));

            System.out.print("¿Desea agregar otro colaborador? (s/n): ");
            continuarColaboradores = sc.nextLine();
        } while (continuarColaboradores.equalsIgnoreCase("s"));

        // Crear el programa e insertarlo en MongoDB
        Programa programa = new Programa(nombre, categoria, horario, audiencias, colaboradores);
        programaDAO.insertarPrograma(programa);

        System.out.println("Programa creado exitosamente.");
    }

    /**
     * Método para consultar la información de un programa al buscarlo por ID
     * @param sc Scanner para la petición de datos
     * @param programaDAO DAO para realizar la consulta a MongoDB
     */
    private static void consultarPrograma(Scanner sc, ProgramaDAOMongoDB programaDAO) {
        System.out.print("Ingrese el ID del programa: ");
        String input = sc.nextLine();
        try{
            Programa programa = programaDAO.buscarProgramaPorId(input);
            if(programa != null){
                System.out.println("Programa encontrado: ");
                System.out.println(programa);
            } else {
                System.out.println("No se ha encontrado el programa");
            }
        } catch (IllegalArgumentException e){
            System.err.println("Formato de ID no válido");
        }
    }

    /**
     * Método para actualizar un programa en el menú
     * @param sc
     * @param programaDAO
     */
    private static void actualizarPrograma(Scanner sc, ProgramaDAOMongoDB programaDAO) {
            System.out.print("Ingrese el ID del programa a actualizar: ");
            String id = sc.nextLine();

            // Buscar el programa por ID
            try{
                Programa programa = programaDAO.buscarProgramaPorId(id);
                if (programa != null) {
                    System.out.println("Programa encontrado: " + programa.getNombre());

                    // Actualizar el nombre del programa
                    System.out.print("Ingrese el nuevo nombre del programa (actual: " + programa.getNombre() + ") ");
                    String nombre = sc.nextLine();
                    if (!nombre.isBlank()) {
                        programa.setNombre(nombre);
                    }

                    // Actualizar la categoría
                    System.out.print("Ingrese la nueva categoría del programa (actual: " + programa.getCategoria() + ") ");
                    String categoria = sc.nextLine();
                    if (!categoria.isBlank()) {
                        programa.setCategoria(categoria);
                    }

                    // Modificar el horario
                    System.out.print("¿Desea modificar el horario? (s/n): ");
                    String modificarHorario = sc.nextLine();
                    if (modificarHorario.equalsIgnoreCase("s")) {
                        System.out.print("Ingrese el nuevo día del horario (actual: " + programa.getHorario().getDia() + "): ");
                        String dia = sc.nextLine();
                        if (!dia.isBlank()) {
                            programa.getHorario().setDia(dia);
                        }

                        LocalTime hora = null;
                        boolean horaValida = false;
                        while (!horaValida) {
                            try {
                                System.out.println("Ingrese la nueva hora del horario (actual: " + programa.getHorario().getHora() + "): ");
                                String horaStr = sc.nextLine();
                                hora = LocalTime.parse(horaStr);  // Convierte el String a LocalTime
                                horaValida = true;
                            } catch (DateTimeParseException e) {
                                System.err.println("Formato de hora incorrecto.");
                            }
                        }
                        programa.getHorario().setHora(hora);
                    }


                    // Agregar nuevos colaboradores
                    System.out.print("¿Desea agregar nuevos colaboradores? (s/n): ");
                    String agregarColaboradores = sc.nextLine();
                    if (agregarColaboradores.equalsIgnoreCase("s")) {
                        do {
                            System.out.print("Ingrese el nombre del colaborador: ");
                            String nombreColaborador = sc.nextLine();

                            System.out.print("Ingrese el rol del colaborador: ");
                            String rolColaborador = sc.nextLine();

                            programa.getColaboradores().add(new Colaborador(nombreColaborador, rolColaborador));

                            System.out.print("¿Desea agregar otro colaborador? (s/n): ");
                        } while (sc.nextLine().equalsIgnoreCase("s"));
                    }

                    // Actualizar el programa en la base de datos
                    programaDAO.actualizarPrograma(programa);
                    System.out.println("Programa actualizado exitosamente.");
                } else {
                    System.out.println("No se encontró un programa con ese ID.");
                }
            } catch (IllegalArgumentException e){
                System.err.println("Formato de ID no válido");
            }
        }

    /**
     * Método para pedir los datos y calcular la audiencia media de un programa
     * @param sc Scanner para capturar los datos
     * @param programaDAO para acceder a la base de datos mongoDB
     */
    private static void calcularMediaAudienciaDeUnPrograma(Scanner sc, ProgramaDAOMongoDB programaDAO) {
        String id;
        System.out.print("Ingrese el ID del programa: ");
        id = sc.nextLine();

        try {
            if(programaDAO.buscarProgramaPorId(id) == null) {
                System.out.println("El programa no existe");
            } else {

                LocalDate fechaInicio = null;
                LocalDate fechaFin = null;
                boolean fechasValidas = false;

                // Bucle para comprobar que las fechas son válidas
                while (!fechasValidas) {
                    try {
                        System.out.println("Ingrese la fecha de inicio (yyyy-mm-dd): ");
                        fechaInicio = LocalDate.parse(sc.nextLine());

                        System.out.println("Ingrese la fecha de fin (yyyy-mm-dd): ");
                        fechaFin = LocalDate.parse(sc.nextLine());

                        fechasValidas = true;
                    } catch (DateTimeParseException e) {
                        System.err.println("Formato de fecha incorrecto. Vuelva a ingresarlas.");
                    }
                }

                double media = programaDAO.calcularProgramaAudienciaMedia(id, fechaInicio, fechaFin);
                System.out.println("Audiencia media: " + media);
            }

        } catch (IllegalArgumentException e){
            System.err.println("Formato de ID no válido");
        }

    }
}

