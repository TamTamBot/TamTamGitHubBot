package com.github.senyast4745.testbot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresJDBCUtils {

    private static Logger log = LoggerFactory.getLogger(PostgresJDBCUtils.class);

    private static String jdbcURL = "jdbc:postgresql://localhost:5432/testgithub";
    private static String jdbcUsername = "botdbuser";
    private static String jdbcPassword = "passwd";


    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            log.error("SQL Connection", e);
        }
        return connection;
    }



    public static void createTable(String createTableSQL) throws SQLException {
        executeUpdate(createTableSQL);
    }


    public static List<Long> executeQuery(String SQLQuery) throws SQLException {
        log.info(SQLQuery);
        // Step 1: Establishing a Connection
        try (Connection connection = PostgresJDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             Statement statement = connection.createStatement();) {

            // Step 3: Execute the query or update query

            ResultSet set = statement.executeQuery(SQLQuery);

            List<Long> repos = new ArrayList<>();
            if(set.next()){
                 repos.add(set.getLong(1));
            } else {
                set.close();
            }
            return repos;


        } catch (SQLException e) {
            // print SQL exception information
            log.error("SQL error in execute: ", e);
            throw e;
        }
    }

    public static void executeUpdate(String SQLQuery) throws SQLException {
        log.info(SQLQuery);
        // Step 1: Establishing a Connection
        try (Connection connection = PostgresJDBCUtils.getConnection();
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



}
