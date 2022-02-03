package com.example.demo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;


public final class MongoConnection {
        private final String uri = ""; // connection string to mongoDB for requests
    protected final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(uri))
            .retryWrites(true)
            .build();
    protected final MongoClient mongoClient = MongoClients.create(settings);
    protected String database = new String();
    protected String collections = new String();

    public MongoConnection(String database, String collections) {
        this.database = database;
        this.collections = collections;
    }

    public Document findValue(String fieldName, String toFind) {
        MongoCollection<Document> coll = this.mongoClient.getDatabase(this.database).getCollection(this.collections);
        return new Document(coll.find(eq(fieldName, toFind)).first());
    }

    public void insertOneValue(Document doc) {
        MongoCollection<Document> coll = this.mongoClient.getDatabase(this.database).getCollection(this.collections);
        try {
            coll.insertOne(doc);
            System.out.println("Success! Inserted document");
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
    }

    public long totalCount(Bson query) {
        MongoCollection<Document> coll = this.mongoClient.getDatabase(this.database).getCollection(this.collections);
        return coll.countDocuments(query);
    }

    public MongoCollection<Document> getColl() {
        return this.mongoClient.getDatabase(this.database).getCollection(this.collections);
    }
}
