package com.jamesmoreton;

import java.util.Optional;
import java.util.UUID;

public interface EmployeePersistenceService {

  void insertEmployee(Employee employee);

  Optional<Employee> getEmployee(UUID employeeUid);

  void deleteEmployee(UUID employeeUid);
}
