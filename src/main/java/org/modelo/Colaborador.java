package org.modelo;

import org.bson.Document;

public class Colaborador {
    // Atributo
    private String nombre;
    private String rol;

    // Constructor
    public Colaborador() {
    }

    public Colaborador(String nombre, String rol) {
        this.nombre = nombre;
        this.rol = rol;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }


    public Document toDocument() {
        return new Document("nombre", nombre)
                .append("rol", rol);
    }

    public static Colaborador fromDocument(Document document) {
        String nombre = document.getString("nombre");
        String rol = document.getString("rol");
        return new Colaborador(nombre, rol);
    }

    @Override
    public String toString() {
        return "\n • Nombre: " + nombre + " | Rol: " + rol;
    }
}
