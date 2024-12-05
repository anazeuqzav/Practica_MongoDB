package org.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Programa {
    // Atributos
    private String id;
    private String nombre;
    private String categoria;
    private Horario horario;
    private List<Audiencia> audiencias;
    private List<Colaborador> colaboradores;

    // Constructores
    public Programa() {
    }
    // Constructor sin ID
    public Programa(String nombre, String categoria, Horario horario, List<Audiencia> audiencias, List<Colaborador> colaboradores) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.horario = horario;
        this.audiencias = audiencias;
        this.colaboradores = colaboradores;
    }

    public Programa(String id, String nombre, String categoria, Horario horario, List<Audiencia> audiencias, List<Colaborador> colaboradores) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.horario = horario;
        this.audiencias = audiencias;
        this.colaboradores = colaboradores;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public List<Audiencia> getAudiencias() {
        return audiencias;
    }

    public void setAudiencias(List<Audiencia> audiencias) {
        this.audiencias = audiencias;
    }

    public List<Colaborador> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(List<Colaborador> colaboradores) {
        this.colaboradores = colaboradores;
    }

    // Métodos

    // Convierte el libro a un documento de MongoDB
    public Document toDocument() {
        // Lista de documentos de audiencias
        List<Document> audienciasDocs = new ArrayList<>();
        for (Audiencia a:audiencias){
            audienciasDocs.add(a.toDocument());
        }

        // Lista de documentos de colaboradores
        List<Document> colaboradoresDocs = new ArrayList<>();
        for (Colaborador c:colaboradores){
            colaboradoresDocs.add(c.toDocument());
        }

        // Devuelve un documento de Programa
        return new Document("nombre", nombre)
                .append("categoria", categoria)
                .append("horario", horario.toDocument())
                .append("audiencias", audienciasDocs)
                .append("colaboradores", colaboradoresDocs);
    }

    // Crear un Programa a partir de un documento de MongoDB
    public static Programa fromDocument(Document document) {
        // Obtener el ID generado por MongoDB
        String id = document.getObjectId("_id").toHexString(); // Obtiene el ID generado por MongoDB

        // Obtener los valores básicos del documento
        String nombre = document.getString("nombre");
        String categoria = document.getString("categoria");

        // Obtener el subdocumento "horario" y convertirlo a un objeto Horario
        Document horarioDoc = document.get("horario", Document.class);
        Horario horario = Horario.fromDocument(horarioDoc); // Usamos el método fromDocument de la clase Horario

        // Obtener la lista de documentos "audiencias" y convertirlos a objetos Audiencia
        List<Document> audienciasDocs = document.getList("audiencias", Document.class);
        List<Audiencia> audiencias = new ArrayList<>();
        if (audienciasDocs != null) {
            for (Document audienciaDoc : audienciasDocs) {
                Audiencia audiencia = Audiencia.fromDocument(audienciaDoc); // Usamos el método fromDocument de la clase Audiencia
                audiencias.add(audiencia);
            }
        }

        // Obtener la lista de documentos "colaboradores" y convertirlos a objetos Colaborador
        List<Document> colaboradoresDocs = document.getList("colaboradores", Document.class);
        List<Colaborador> colaboradores = new ArrayList<>();
        if (colaboradoresDocs != null) {
            for (Document colaboradorDoc : colaboradoresDocs) {
                Colaborador colaborador = Colaborador.fromDocument(colaboradorDoc); // Usamos el método fromDocument de la clase Colaborador
                colaboradores.add(colaborador);
            }
        }

        // Crear y devolver el objeto Programa
        return new Programa(id, nombre, categoria, horario, audiencias, colaboradores);

    }

    @Override
    public String toString() {

        return "---------------------------------------------\n" +
                "Programa: " + "\n" +
                "ID:              " + id + "\n" +
                "Nombre:          " + nombre + "\n" +
                "Categoria:       " + categoria + "\n" +
                "Horario:         " + horario + "\n" +
                "Audiencias:      " + audiencias + "\n" +
                "Colaboradores:   " + colaboradores + "\n";
    }
}
