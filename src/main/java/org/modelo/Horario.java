package org.modelo;

import org.bson.Document;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Horario {
    // Atributos
    private String dia;
    private LocalTime hora; // TODO si me da tiempo usar un objeto para la hora

    // Constructor
    public Horario() {
    }

    public Horario(String dia, LocalTime hora) {
        this.dia = dia;
        this.hora = hora;
    }

    // Getters y setters
    public String getDia() {
        return dia;
    }
    public void setDia(String dia) {
        this.dia = dia;
    }
    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    // Métodos
    public Document toDocument() {
        String horaString = hora.format(DateTimeFormatter.ISO_LOCAL_TIME); // pasa la hora en formato LocalTime a String
        return new Document("dia", dia)
                .append("hora", horaString);
    }
    public static Horario fromDocument(Document document) {
        String dia = document.getString("dia");
        String horaString = document.getString("hora");
        // Convertir el String de la hora en LocalTime
        LocalTime hora = LocalTime.parse(horaString, DateTimeFormatter.ISO_LOCAL_TIME);
        return new Horario(dia, hora);
    }

    @Override
    public String toString() {
        return "Dia: " + dia + " | Hora: " + hora;
    }
}
