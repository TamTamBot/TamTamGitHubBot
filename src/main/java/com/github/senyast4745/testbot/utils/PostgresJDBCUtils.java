package com.github.senyast4745.testbot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresJDBCUtils {

    private static Logger log = LoggerFactory.getLogger(PostgresJDBCUtils.class);

//    private static String jdbcURL = "jdbc:postgresql://localhost:5432/testgithub";
    private final String jdbcURL;
//    private static String jdbcUsername = "botdbuser";
    private final String jdbcUsername;
//    private static String jdbcPassword = "passwd";
    private final  String jdbcPassword;

    private static volatile PostgresJDBCUtils instance;

    private PostgresJDBCUtils(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            log.error("SQL Connection", e);
        }
        return connection;
    }


    public void createTable(String createTableSQL) throws SQLException {
        executeUpdate(createTableSQL);
    }


    @SuppressWarnings("unchecked")
    public <T> List<T> executeQuery(String SQLQuery) throws SQLException {
        log.info(SQLQuery);
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query

            ResultSet set = statement.executeQuery(SQLQuery);

            List<T> repos = new ArrayList<>();
            while (set.next()) {

                repos.add((T) set.getObject(1));
//                 log.info ();
            }
            set.close();
            return repos;


        } catch (SQLException e) {
            // print SQL exception information
            log.error("SQL error in execute: ", e);
            throw e;
        }
    }

    public void executeUpdate(String SQLQuery) throws SQLException {
        log.info(SQLQuery);
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update querstatement.executeUpdate(SQLQuery);
            statement.executeUpdate(SQLQuery);
            statement.cancel();

        } catch (SQLException e) {
            // print SQL exception information
            log.error("SQL error in execute: ", e);
            throw e;
        }
    }

    public static PostgresJDBCUtils getInstance(String jdbcUrl, String jdbcUser, String jdbcPassword){
       PostgresJDBCUtils localInstance = instance;
       if(localInstance == null){
           synchronized (PostgresJDBCUtils.class){
               localInstance = instance;
               if(localInstance == null){
                   localInstance = new PostgresJDBCUtils(jdbcUrl, jdbcUser,  jdbcPassword);
                   instance = localInstance;
               }
           }
       }
       return localInstance;
    }


}
