package org.modelo;

import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class Audiencia {
    // Atributos
    private LocalDate fecha;
    private int espectadores;

    // Constructor
    public Audiencia() {
    }

    public Audiencia(LocalDate fecha, int espectadores) {
        this.fecha = fecha;
        this.espectadores = espectadores;
    }

    // Getters y setters
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getEspectadores() {
        return espectadores;
    }

    public void setEspectadores(int espectadores) {
        this.espectadores = espectadores;
    }

    public Document toDocument() {
        Date fechaDate =Date.from(fecha.atStartOfDay(ZoneOffset.UTC).toInstant());; // convierte el LocalDate a Date compatible con mongoDB
        return new Document("fecha", fechaDate)
                .append("espectadores", espectadores);
    }

    public static Audiencia fromDocument(Document document) {
        Date fechaDate = document.getDate("fecha"); // Extrae la fecha de MongoDB en tipo Date
        LocalDate fecha = fechaDate.toInstant()
                .atZone(ZoneOffset.UTC)  // Fuerza la zona horaria UTC
                .toLocalDate();  // Convierte a LocalDate
        int espectadores = document.getInteger("espectadores");
        return new Audiencia(fecha, espectadores);
    }

    @Override
    public String toString() {
        return "\n • Fecha: " + fecha + " | Espectadores: " + espectadores;

    }
}

