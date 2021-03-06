package com.jamesmoreton.postgres;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jamesmoreton.Employee;
import com.jamesmoreton.EmployeePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class PostgresEmployeePersistenceService implements EmployeePersistenceService {

  private static final Logger logger = LoggerFactory.getLogger(PostgresEmployeePersistenceService.class);

  private final PostgresDatabase postgresDatabase;

  @Inject
  public PostgresEmployeePersistenceService(PostgresDatabase postgresDatabase) {
    this.postgresDatabase = postgresDatabase;
  }

  @Override
  public void insertEmployee(Employee employee) {
    int insert = postgresDatabase.runInsertUpdateOrDeleteQuery(
        String.format("INSERT INTO employee (employee_uid, first_name, last_name, date_of_birth, role) " +
            "VALUES ('%s', '%s', '%s', '%s', '%s');", employee.getEmployeeUid(), employee.getFirstName(), employee.getLastName(), employee.getDateOfBirth(), employee.getRole())
    );

    if (insert == 1) {
      logger.info("Employee {} inserted", employee.getEmployeeUid());
    }
  }

  @Override
  public Optional<Employee> getEmployee(UUID employeeUid) {
    List<List<String>> result = postgresDatabase.runSelectQuery(
        String.format("SELECT employee_uid, first_name, last_name, date_of_birth, role " +
            "FROM employee WHERE employee_uid = '%s';", employeeUid)
    );

    return switch (result.size()) {
      case 0 -> Optional.empty();
      case 1 -> Optional.of(mapEmployeeRecord(result.get(0)));
      default -> throw new RuntimeException(String.format("Expected zero to one elements, [%s] returned", result.size()));
    };
  }

  @Override
  public void deleteEmployee(UUID employeeUid) {
    int delete = postgresDatabase.runInsertUpdateOrDeleteQuery(
        String.format("DELETE FROM employee WHERE employee_uid = '%s';", employeeUid)
    );

    if (delete == 1) {
      logger.info("Employee {} deleted", employeeUid);
    }
  }

  private Employee mapEmployeeRecord(List<String> employee) {
    return new Employee(
        UUID.fromString(employee.get(0)),
        employee.get(1),
        employee.get(2),
        LocalDate.parse(employee.get(3)),
        Employee.Role.valueOf(employee.get(4))
    );
  }
}
