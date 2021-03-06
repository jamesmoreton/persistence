package com.jamesmoreton.mongo;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jamesmoreton.Employee;
import com.jamesmoreton.EmployeePersistenceService;
import com.jamesmoreton.mongo.MongoMainline.MongoModule.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

@Singleton
public class MongoEmployeePersistenceService implements EmployeePersistenceService {

  private static final Logger logger = LoggerFactory.getLogger(MongoEmployeePersistenceService.class);

  private final MongoCollection<Document> employees;

  @Inject
  public MongoEmployeePersistenceService(
      @Database MongoDatabase database
  ) {
    this.employees = database.getCollection("employee");
  }

  @Override
  public void insertEmployee(Employee employee) {
    InsertOneResult result = employees.insertOne(toEmployeeDocument(employee));

    if (result.wasAcknowledged()) {
      logger.info("Employee {} inserted", employee.getEmployeeUid());
    }
  }

  @Override
  public Optional<Employee> getEmployee(UUID employeeUid) {
    return fromEmployeeDocument(employees.find(eq("employee_uid", employeeUid)).first());
  }

  @Override
  public void deleteEmployee(UUID employeeUid) {
    DeleteResult result = employees.deleteOne(eq("employee_uid", employeeUid));

    if (result.getDeletedCount() == 1) {
      logger.info("Employee {} deleted", employeeUid);
    }
  }

  private Document toEmployeeDocument(Employee employee) {
    return new Document("_id", new ObjectId())
        .append("employee_uid", employee.getEmployeeUid())
        .append("first_name", employee.getFirstName())
        .append("last_name", employee.getLastName())
        .append("date_of_birth", employee.getDateOfBirth())
        .append("role", employee.getRole().name());
  }

  private Optional<Employee> fromEmployeeDocument(Document doc) {
    if (doc == null) {
      return Optional.empty();
    }

    return Optional.of(new Employee(
        doc.get("employee_uid", UUID.class),
        doc.getString("first_name"),
        doc.getString("last_name"),
        LocalDate.ofInstant(doc.getDate("date_of_birth").toInstant(), ZoneId.systemDefault()),
        Employee.Role.valueOf(doc.getString("role"))
    ));
  }
}
