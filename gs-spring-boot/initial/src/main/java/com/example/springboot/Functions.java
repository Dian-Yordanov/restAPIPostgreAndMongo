package com.example.springboot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

public class Functions {

    public Functions(){

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

