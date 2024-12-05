package org.dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.modelo.Audiencia;
import org.modelo.Programa;
import org.utils.MongoDBConnection;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ProgramaDAOMongoDB implements ProgramaDAO{
    private final MongoCollection<Document> coleccion;

    public ProgramaDAOMongoDB() {
        MongoDBConnection connection = new MongoDBConnection();
        this.coleccion = connection.getCollection("programas");
    }

    /**
     * Método para insertar un programa en la coleccion programas
     * @param programa objeto programa a insertar
     */
    @Override
    public void insertarPrograma(Programa programa) {
        Document documento = programa.toDocument();
        coleccion.insertOne(documento);
    }

    /**
     * Método para buscar un programa por un ID
     * @param id ID para buscar un programa concreto por un ID
     * @return objeto Programa
     */
    @Override
    public Programa buscarProgramaPorId(String id) {
        // filtra por id y devuelve una lista de documentos, por eso se filtra por .first()
        Document document = coleccion.find(eq("_id", new ObjectId(id))).first();
        // devuelve el objeto Programa o null si no se encuentra
        return document != null ? Programa.fromDocument(document) : null;
    }

    /**
     * Método para recuperar todos los programas en la base de datos
     * @return una lista de programas
     */
    @Override
    public List<Programa> buscarTodosProgramas() {
        List<Programa> programas = new ArrayList<>();
        for (Document document : coleccion.find()) {
            programas.add(Programa.fromDocument(document));
        }
        return programas;
    }

    @Override
    public void actualizarPrograma(Programa programa) {
        Document filter = new Document("_id", new ObjectId(programa.getId()));
        Document update = new Document("$set", programa.toDocument());
        coleccion.updateOne(filter, update);
    }


    @Override
    public List<Programa> buscarPorProgramaCategoria(String categoria) {
        List<Programa> programas = new ArrayList<>();
        for (Document document : coleccion.find(eq("categoria", categoria))) {
            programas.add(Programa.fromDocument(document));
        }
        return programas;
    }


    @Override
    public Programa programaConMayorAudiencia(LocalDate fecha) {
        List<Programa> programas = buscarTodosProgramas();  // Lista de todos los programas
        Programa programaConMayorAudiencia = null;
        int maxEspectadores = 0;

        // Recorre todos los programas
        for (Programa p : programas) {
            List<Audiencia> audiencias = p.getAudiencias();  // Lista de audiencias del programa
            // Recorre las audiencias del programa p
            for (Audiencia a : audiencias) {
                    LocalDate fechaAudiencia = a.getFecha();
                if (fechaAudiencia.equals(fecha)) {
                    // Si tiene más espectadores, actualiza el programa con mayor audiencia
                    if (a.getEspectadores() > maxEspectadores) {
                        maxEspectadores = a.getEspectadores();
                        programaConMayorAudiencia = p;
                    }
                }
            }
        }

        return programaConMayorAudiencia;
    }

    @Override
    public double calcularProgramaAudienciaMedia(String id, LocalDate fechaInicio, LocalDate fechaFin) {
        double totalEspectadores = 0;
        int totalAudiencias = 0;
        // Busca el programa por su ID
        Programa programa = buscarProgramaPorId(id);
        if (programa != null) {
            // Recorre las audiencias del programa
            for (Audiencia audiencia : programa.getAudiencias()) {

                LocalDate fechaAudiencia = audiencia.getFecha();

                // Verifica si la audiencia está dentro del rango de fechas
                if (!fechaAudiencia.isBefore(fechaInicio) && !fechaAudiencia.isAfter(fechaFin)) {
                    totalEspectadores += audiencia.getEspectadores(); // añade los espectadores al total
                    totalAudiencias++; // va contando el número de audiencias
                }
            }

            // Calcula la audiencia media
            if (totalAudiencias > 0) {
                return totalEspectadores / totalAudiencias; // devuelve la media
            }
        }

        return 0; // devuelve 0 si no hay registradas audiencias o el programa no se encuentra
    }

    /**
     * Elimina un programa de la base de datos dado su ID.
     * @param id ID del programa a eliminar.
     */
    public void eliminarPrograma(String id) {
        try {
            // Crea filtro para encontrar el programa por su ID
            Document filter = new Document("_id", new ObjectId(id));
            coleccion.deleteOne(filter); // elimina el programa
            System.out.println("Programa eliminado exitosamente.");

        } catch (Exception e) {
            System.out.println("Ocurrió un error al intentar eliminar el programa: " + e.getMessage());
        }
    }

}
