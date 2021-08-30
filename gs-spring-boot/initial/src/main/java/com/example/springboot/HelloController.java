package com.example.springboot;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

@RestController
public class HelloController {

	@GetMapping("/books")
	public String booksIndex() throws SQLException {
		MongoDBOperations mongo = new MongoDBOperations();
		PostgreDatabaseOperations booksPostgreDB = new PostgreDatabaseOperations();

		//	Delete any previous databases
		mongo.deleteAllDocuments(mongo.MongoDBOperationsMainFunction());
		booksPostgreDB.dropAllTables();

		//	Insert a dataset of authors and books into MongoDB and PostgreDB. Multiple authors can have worked on a book
		// and books can have multiple authors and the data sets reflect that. The "Author" and "Book" objects
		// are actually very close to simple HashMap mappings ( with a minor additional functionality in the "Book" object),
		// but objects are easier to work with.
		mongo.insertAuthorAndBook(mongo.MongoDBOperationsMainFunction(), Arrays.asList(
				new Author("Arthur C. Clarke", "2001: A Space Odyssey"),
				new Author("Ivan Vazov", "Under the Yoke"),
				new Author("Mark Ripkowski", "Epic of the Forgotten"),
				new Author("Stephen King", "The Talisman"),
				new Author("Stephen King", "It"),
				new Author("Peter Straub", "The Talisman"),
				new Author("Ivan Vazov", "Epic of the Forgotten")));
		booksPostgreDB.createTable();
		booksPostgreDB.insertUsers(Arrays.asList(
				new Book("2001: A Space Odyssey", "Arthur C. Clarke"),
				new Book("Epic of the Forgotten", "Ivan Vazov"),
				new Book("Epic of the Forgotten", "Mark Ripkowski"),
				new Book("The Talisman", "Stephen King"),
				new Book("The Talisman", "Peter Straub"),
				new Book("It", "Stephen King"),
				new Book("Under the Yoke","Ivan Vazov")));

		//	remove duplicates and initialise the data sets.
		HashMap<String, Book> booksSet = booksPostgreDB.returnInfo();
		booksSet = Functions.booksSetNoDuplicates(booksSet);

		final JsonArray JsonArrayToReturn = new JsonArray();

		String returnString = "";

		ArrayList<String> booksID = new ArrayList<String>();
		ArrayList<String> booksName = new ArrayList<String>();
		ArrayList<String> booksCorrespondingSet = new ArrayList<String>();

		// get the mapping for books, later this mapping with be used to map each book to all authors
		for (Map.Entry<String, Book> bookMapping : booksSet.entrySet()){
			String mongoReturn = mongo.getAuthorByBook(mongo.MongoDBOperationsMainFunction(), bookMapping.getValue().getBookName());
			booksID.add(bookMapping.getKey());
			booksName.add(bookMapping.getValue().getBookName());
			booksCorrespondingSet.add(mongoReturn);
		}

		//	having each bookMapping, now we map it to the set of authors returned from MongoDB.
		//	All of these mappings are turned JsonObjects that are returned in a particular order : "id" -> "name" ->"authors"
		//	All the JsonObjects are finally added to a JsonArray.
		for(int i=0;i<booksID.size();i++){

			String booksAuthorsInfo = Functions.fixedMongoDBAuthorsArray(booksCorrespondingSet.get(i));
			final JsonArray JsonbooksAuthors = new Gson().fromJson(booksAuthorsInfo, JsonArray.class);

			JsonObject responseObj = new JsonObject();
			responseObj.addProperty("id", booksID.get(i));
			responseObj.addProperty("name", booksName.get(i));
			responseObj.add("authors", JsonbooksAuthors);

			JsonArrayToReturn.add(responseObj);
		}

		// The final JsonArray is casted into string and returned as the endpoint API response.

		returnString = JsonArrayToReturn.toString();
		return returnString;
	}



}