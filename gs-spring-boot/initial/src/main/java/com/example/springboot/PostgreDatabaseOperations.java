package com.example.springboot;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostgreDatabaseOperations {
    // Used https://stackoverflow.com/questions/64210167/unable-to-connect-to-postgres-db-due-to-the-authentication-type-10-is-not-suppor
    // to solve ssl problems. Not secure but works fine for now.

    private final String url = "jdbc:postgresql://localhost/postgres";
    private final String user = "postgres";
    private final String password = " ";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void dropAllTables() throws SQLException{

        Connection conn = DriverManager.getConnection(url, user, password);
        Statement statement = conn.createStatement();

        String createSql = "DO $$ \n" +
                "  DECLARE \n" +
                "    r RECORD;\n" +
                "BEGIN\n" +
                "  FOR r IN \n" +
                "    (\n" +
                "      SELECT table_name \n" +
                "      FROM information_schema.tables \n" +
                "      WHERE table_schema=current_schema()\n" +
                "    ) \n" +
                "  LOOP\n" +
                "     EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.table_name) || ' CASCADE';\n" +
                "  END LOOP;\n" +
                "END $$ ;";

        statement.executeUpdate(createSql);

    }

    public void createTable() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password);
        Statement statement = conn.createStatement();
        String createSql = "CREATE TABLE books (\n" +
                "\tbookId serial PRIMARY KEY, \n" +
                "\tbookName VARCHAR ( 50 ) NOT NULL,\n" +
                "\tbookAuthor VARCHAR ( 50 ) \n" +
                ");";

        statement.executeUpdate(createSql);

    }

    public void insertUsers(List< Book > list) {
        try (
                Connection conn = DriverManager.getConnection(url, user, password);

                PreparedStatement statement = conn.prepareStatement("INSERT INTO books(bookName, bookAuthor) VALUES (?, ?)");
        ) {
            for (Book book: list) {
                statement.setString(1, book.getBookName());
                statement.setString(2, book.getAuthor());

                statement.addBatch();

                System.out.println("The prepared statement to executed is: " + statement);
                statement.executeBatch();

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public HashMap<String, Book> returnInfo() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password);
        Statement statement = conn.createStatement();
        String createSql = "SELECT * FROM books";

        ResultSet rs = statement.executeQuery(createSql);
        HashMap<String, Book> booksSet = new HashMap<String, Book>();

        while (rs.next()) {
            String bookId = rs.getString("bookId");
            String bookName = rs.getString("bookName");
            String bookAuthor = rs.getString("bookAuthor");

            booksSet.put(bookId, new Book(bookName, bookAuthor));

//            System.out.println(bookName + ", " + bookAuthor);
        }


        return booksSet;
    }


}
