package com.jamesmoreton;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Singleton
public class EmployeeService {

  private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

  private final EmployeePersistenceService employeePersistenceService;

  @Inject
  public EmployeeService(EmployeePersistenceService employeePersistenceService) {
    this.employeePersistenceService = employeePersistenceService;
  }

  public void doThings() {
    // Insert employees
    Employee employee1 = EmployeeFactory.create();
    Employee employee2 = EmployeeFactory.create();
    employeePersistenceService.insertEmployee(employee1);
    employeePersistenceService.insertEmployee(employee2);

    // Get employees
    employeePersistenceService.getEmployee(employee1.getEmployeeUid()).ifPresentOrElse(
        e -> logger.info("Employee found: {}", e),
        () -> {
          throw new RuntimeException("Could not find employee " + employee1.getEmployeeUid());
        });
    employeePersistenceService.getEmployee(employee2.getEmployeeUid()).ifPresentOrElse(
        e -> logger.info("Employee found: {}", e),
        () -> {
          throw new RuntimeException("Could not find employee " + employee2.getEmployeeUid());
        });

    // Delete employees
    employeePersistenceService.deleteEmployee(employee1.getEmployeeUid());
    employeePersistenceService.deleteEmployee(employee2.getEmployeeUid());

    // Get employees again
    Optional<Employee> result3 = employeePersistenceService.getEmployee(employee1.getEmployeeUid());
    Optional<Employee> result4 = employeePersistenceService.getEmployee(employee2.getEmployeeUid());
    if (result3.isPresent() || result4.isPresent()) {
      throw new RuntimeException("Employees found");
    }
  }
}
