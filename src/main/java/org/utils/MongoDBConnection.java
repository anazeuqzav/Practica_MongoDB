package org.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBConnection {
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoDBConnection() {
        // Conectar a MongoDB en localhost:27017
        mongoClient = MongoClients.create("mongodb://localhost:27017");

        // Conectar a la base de datos "television"
        database = mongoClient.getDatabase("television");
    }

    // Método para obtener una colección específica
    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    // Método para cerrar la conexión
    public void close() {
        mongoClient.close();
    }
}