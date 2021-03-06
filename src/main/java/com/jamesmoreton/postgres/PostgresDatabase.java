package com.jamesmoreton.postgres;

import com.google.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PostgresDatabase {

  private static final String DATABASE_PORT = "15432";
  private static final String DATABASE_NAME = "persistence";
  private static final String JDBC_DRIVER = "org.postgresql.Driver";
  private static final String DB_URL = String.format("jdbc:postgresql://localhost:%s/%s", DATABASE_PORT, DATABASE_NAME);
  private static final String USER = "test";
  private static final String PASS = "test";

  public int runInsertUpdateOrDeleteQuery(String query) {
    Connection connection = getConnection();

    try (Statement statement = connection.createStatement()) {
      return executeUpdate(query, statement);
    } catch (SQLException e) {
      throw new RuntimeException("Failed to create statement", e);
    }
  }

  public List<List<String>> runSelectQuery(String query) {
    Connection connection = getConnection();

    try (Statement statement = connection.createStatement()) {
      return runAndParseQuery(query, statement);
    } catch (SQLException e) {
      throw new RuntimeException("Failed to create statement", e);
    }
  }

  private Connection getConnection() {
    try {
      Class.forName(JDBC_DRIVER);
      return DriverManager.getConnection(DB_URL, USER, PASS);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Failed to get JDBC driver", e);
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get connection to " + DB_URL, e);
    }
  }

  private List<List<String>> runAndParseQuery(String query, Statement statement) {
    List<List<String>> results = new ArrayList<>();

    try (ResultSet rs = executeQuery(query, statement)) {
      int columnCount = rs.getMetaData().getColumnCount();

      while (rs.next()) {
        List<String> resultRow = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
          resultRow.add(rs.getString(i));
        }
        results.add(resultRow);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to access result set of query", e);
    }

    return results;
  }

  private ResultSet executeQuery(String query, Statement statement) {
    try {
      return statement.executeQuery(query);
    } catch (SQLException e) {
      throw new RuntimeException(String.format("Failed to execute query statement [%s]", query), e);
    }
  }

  private int executeUpdate(String query, Statement statement) {
    try {
      return statement.executeUpdate(query);
    } catch (SQLException e) {
      throw new RuntimeException(String.format("Failed to execute update statement [%s]", query), e);
    }
  }
}
