package com.example.springboot;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.asList;

public class MongoDBOperations {

    public void MongoDBOperations() {

    }

    public MongoDatabase MongoDBOperationsMainFunction(){
        ConnectionString connectionString = new ConnectionString("mongodb+srv://books:qqqq1111@cluster0.3130e.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase sampleTrainingDB = mongoClient.getDatabase("books");

        return sampleTrainingDB;
    }

    public static void insertAuthorAndBook(MongoDatabase sampleTrainingDB, List< Author > list) {
        MongoCollection<Document> bookCollection = sampleTrainingDB.getCollection("book");

        for (Author author: list) {

            Document bookDocument = new Document("_id", new ObjectId());
            bookDocument.append("author_id", author.getAuthor()).append("book_id", author.getBookName());
            bookCollection.insertOne(bookDocument);

        }
    }

    public static String getAuthorAndBook(MongoDatabase sampleTrainingDB) {
        MongoCollection<Document> bookCollection = sampleTrainingDB.getCollection("book");
        Collection<JSONObject> items = new ArrayList<JSONObject>();

        MongoCursor<Document> cursor = bookCollection.find().iterator();
        try {
            while (cursor.hasNext()) {

                JSONObject item = new JSONObject(cursor.next().toJson());
                items.add(item);
            }
        } finally {
            cursor.close();
        }

        return items.toString();
    }

    public static void deleteAllDocuments(MongoDatabase sampleTrainingDB) {
        sampleTrainingDB.getCollection("book").drop();
    }

    public static String getAuthorByBook(MongoDatabase sampleTrainingDB, String bookNameToLookFor) {
        MongoCollection<Document> bookCollection = sampleTrainingDB.getCollection("book");
        Collection<JSONObject> items = new ArrayList<JSONObject>();

        Bson filter = eq("book_id", bookNameToLookFor);

        MongoCursor<Document> cursor = bookCollection.find(filter).iterator();
        try {
            while (cursor.hasNext()) {

                JSONObject item = new JSONObject(cursor.next().toJson());
                items.add(item);
            }
        } finally {
            cursor.close();
        }

        return items.toString();
    }


}
