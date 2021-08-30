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

	@GetMapping("/")
	public String index() throws SQLException {


		String returnString = "Greetings";

		PostgreDatabaseOperations booksPostgreDB = new PostgreDatabaseOperations();
		booksPostgreDB.createTable();

		return returnString;
	}

	@GetMapping("/delete")
	public String delete() throws SQLException {

		String returnString = "Deleted";

		PostgreDatabaseOperations booksPostgreDB = new PostgreDatabaseOperations();
		booksPostgreDB.dropAllTables();

		return returnString;
	}

	@GetMapping("/insert")
	public String insert() throws SQLException {

		String returnString = "Inserted";

		PostgreDatabaseOperations booksPostgreDB = new PostgreDatabaseOperations();
		booksPostgreDB.insertUsers(Arrays.asList(
				new Book("2001: A Space Odyssey", "Arthur C. Clarke"),
				new Book("Epic of the Forgotten", "Ivan Vazov"),
				new Book("Epic of the Forgotten", "Mark Ripkowski"),
				new Book("Under the Yoke","Ivan Vazov")));

		return returnString;
	}

	@GetMapping("/returnInfo")
	public String returnInfo() throws SQLException {

		PostgreDatabaseOperations booksPostgreDB = new PostgreDatabaseOperations();

		HashMap<String, Book> booksSet = booksPostgreDB.returnInfo();
		JSONObject booksSetJSON = new JSONObject();
		for (Map.Entry<String, Book> bookMapping : booksSet.entrySet()){
			String tempBookObjectMapping = bookMapping.getValue().getBookName() + " - " + bookMapping.getValue().getAuthor();
			booksSetJSON.put(bookMapping.getKey(), tempBookObjectMapping);
		}

		String postgreReturn = booksSetJSON.toString();
		return postgreReturn;
	}

	@GetMapping("/mongoCreateTable")
	public String mongoCreateTable() throws SQLException {

		String returnString = "Greetings mongoCreateTable";

		MongoDBOperations mongo = new MongoDBOperations();
		mongo.insertAuthorAndBook(mongo.MongoDBOperationsMainFunction(), Arrays.asList(new Author("n", "n")));


		return returnString;
	}

	@GetMapping("/mongoGetTable")
	public String mongoGetTable() throws SQLException {

		String returnString = "Greetings mongoCreateTable";

		MongoDBOperations mongo = new MongoDBOperations();

		String result = mongo.getAuthorAndBook(mongo.MongoDBOperationsMainFunction());
		System.out.println("Result: " + result);

		return result;
	}

	@GetMapping("/mongoDeleteTable")
	public String mongoDeleteTable() throws SQLException {

		String returnString = "Deleted";

		MongoDBOperations mongo = new MongoDBOperations();
		mongo.deleteAllDocuments(mongo.MongoDBOperationsMainFunction());

		return returnString;
	}

	@GetMapping("/OLDbooks")
	public String OLDooksIndex() throws SQLException {
		String returnString = "Greetings mongoCreateTable";

		MongoDBOperations mongo = new MongoDBOperations();
		PostgreDatabaseOperations booksPostgreDB = new PostgreDatabaseOperations();

		mongo.deleteAllDocuments(mongo.MongoDBOperationsMainFunction());
		booksPostgreDB.dropAllTables();

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


		String mongoReturn = mongo.getAuthorAndBook(mongo.MongoDBOperationsMainFunction());

		HashMap<String, Book> booksSet = booksPostgreDB.returnInfo();
		JSONObject booksSetJSON = new JSONObject();
		for (Map.Entry<String, Book> bookMapping : booksSet.entrySet()){
			String tempBookObjectMapping = bookMapping.getValue().getBookName() + " - " + bookMapping.getValue().getAuthor();
			booksSetJSON.put(bookMapping.getKey(), tempBookObjectMapping);
		}

		String postgreReturn = booksSetJSON.toString();

		return " MONGO: " + mongoReturn + " POSTGRE: " + postgreReturn;
	}

	@GetMapping("/books")
	public String booksIndex() throws SQLException {
		MongoDBOperations mongo = new MongoDBOperations();
		PostgreDatabaseOperations booksPostgreDB = new PostgreDatabaseOperations();

		mongo.deleteAllDocuments(mongo.MongoDBOperationsMainFunction());
		booksPostgreDB.dropAllTables();

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

		HashMap<String, Book> booksSet = booksPostgreDB.returnInfo();
		booksSet = booksSetNoDuplicates(booksSet);

		final JsonArray JsonArrayToReturn = new JsonArray();

		String returnString = "";

		ArrayList<String> booksID = new ArrayList<String>();
		ArrayList<String> booksName = new ArrayList<String>();
		ArrayList<String> booksCorrespondingSet = new ArrayList<String>();

		for (Map.Entry<String, Book> bookMapping : booksSet.entrySet()){
			String mongoReturn = mongo.getAuthorByBook(mongo.MongoDBOperationsMainFunction(), bookMapping.getValue().getBookName());
			booksID.add(bookMapping.getKey());
			booksName.add(bookMapping.getValue().getBookName());
			booksCorrespondingSet.add(mongoReturn);
		}

		for(int i=0;i<booksID.size();i++){
//			System.out.println("booksID:          					" + booksID.get(i));
//			System.out.println("booksName:          				" + booksName.get(i));
//			System.out.println("booksCorrespondingSet:          	" + booksCorrespondingSet.get(i));

			String booksAuthorsInfo = fixedMongoDBAuthorsArray(booksCorrespondingSet.get(i));
			final JsonArray JsonbooksAuthors = new Gson().fromJson(booksAuthorsInfo, JsonArray.class);

			JsonObject responseObj = new JsonObject();
			responseObj.addProperty("id", booksID.get(i));
			responseObj.addProperty("name", booksName.get(i));
			responseObj.add("authors", JsonbooksAuthors);

			JsonArrayToReturn.add(responseObj);

		}

		returnString = JsonArrayToReturn.toString();

		return returnString;
	}

	public static HashMap<String, Book> booksSetNoDuplicates(HashMap<String, Book> bookSet){

		HashMap<String, Book> indexToBookMapping = new HashMap<String, Book>();
		ArrayList<String> ArrayOfUniqueNames = new ArrayList<String>();

		for (Map.Entry<String, Book> bookMapping : bookSet.entrySet()){
			if(!ArrayOfUniqueNames.contains(bookMapping.getValue().getBookName())){
				ArrayOfUniqueNames.add(bookMapping.getValue().getBookName());
				HashMap<String, String> bookHashMapping = new HashMap<String, String>();
				bookHashMapping.put(bookMapping.getValue().getBookName(), bookMapping.getValue().getAuthor());
				indexToBookMapping.put(bookMapping.getKey(), new Book(bookMapping.getValue().getBookName(), bookMapping.getValue().getAuthor()));
			}
		}

		return indexToBookMapping;
	}

	public static String fixedMongoDBAuthorsArray(String stringToFix){
		String stringArray = stringToFix;
		stringArray = stringToFix.substring(1,stringArray.length()-1);
		System.out.println("stringArray                     	" + stringArray);
		String idString1 = "";
		String idString2 = "";
		String idString3 = "";
		String authors = "";

		JsonArray JsonArrayToReturn = new JsonArray();

		String[] authorsSplittedString = stringArray.split(", ");

		List<String> species = Arrays.asList(authorsSplittedString);

		System.out.println("WHOLE ARRAY                     	" + species);

		for (int i=0;i< species.size();i++) {
			JsonObject responseObj = new JsonObject();
			System.out.println("INDEX " + i + "                      " + species.get(i));

			String stringForNewArray = species.get(i).substring(1, species.get(i).length()-1);
			String[] authorsSplittedString1 = stringForNewArray.split(",");
			List<String> species1 = Arrays.asList(authorsSplittedString1);
			System.out.println("species1                     	" + species1);

			idString1 = species1.get(0).replace("_id={", "");
			idString2 = idString1.substring(0, idString1.length() - 1);
			idString3 = idString2.replace("\"_id\":{\"$oid\":", "");

			authors = species1.get(2).split(":")[1];

			System.out.println("id                     		" + idString3);
			System.out.println("authors                     " + authors);

			responseObj.addProperty("id", idString3);
			responseObj.addProperty("authors", authors);
			JsonArrayToReturn.add(responseObj);

		}

		stringArray = JsonArrayToReturn.toString();
		stringArray = stringArray.replace("\\\"","");
		System.out.println("!!!stringArray                     " + stringArray);

		return stringArray;
	}


}