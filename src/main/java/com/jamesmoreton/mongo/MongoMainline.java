package com.jamesmoreton.mongo;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.jamesmoreton.EmployeePersistenceService;
import com.jamesmoreton.EmployeeService;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class MongoMainline {

  private static final Logger logger = LoggerFactory.getLogger(MongoMainline.class);

  private static final int DATABASE_PORT = 26543;
  private static final String DATABASE_NAME = "persistence";

  private static MongoDatabase database;

  public static class MongoModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(EmployeePersistenceService.class).to(MongoEmployeePersistenceService.class);
    }

    @BindingAnnotation
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RUNTIME)
    public @interface Database {
    }

    @Provides
    @Singleton
    @Database
    public MongoDatabase database() {
      return database;
    }
  }

  public static void main(String[] args) {
    MongoClient mongoClient = createDatabase();

    Injector injector = Guice.createInjector(new MongoModule());
    EmployeeService employeeService = injector.getInstance(EmployeeService.class);
    employeeService.doThings();

    mongoClient.close();
    logger.info("Successfully closed database client");
  }

  private static MongoClient createDatabase() {
    logger.info("Creating database client on port {}", DATABASE_PORT);
    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString("mongodb://localhost:" + DATABASE_PORT))
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .build();
    MongoClient mongoClient = MongoClients.create(settings);
    database = mongoClient.getDatabase(DATABASE_NAME);
    logger.info("Successfully created database client on port {}", DATABASE_PORT);
    return mongoClient;
  }
}
