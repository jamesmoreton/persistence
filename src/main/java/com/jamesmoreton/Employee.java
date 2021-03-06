package com.jamesmoreton;

import java.time.LocalDate;
import java.util.UUID;

public class Employee {

  public enum Role {
    ENGINEER,
    PRODUCT,
    HR
  }

  private final UUID employeeUid;
  private final String firstName;
  private final String lastName;
  private final LocalDate dateOfBirth;
  private final Role role;

  public Employee(
      UUID employeeUid,
      String firstName,
      String lastName,
      LocalDate dateOfBirth,
      Role role
  ) {
    this.employeeUid = employeeUid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.role = role;
  }

  public UUID getEmployeeUid() {
    return employeeUid;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public Role getRole() {
    return role;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "employeeUid=" + employeeUid +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", dateOfBirth=" + dateOfBirth +
        ", role=" + role +
        '}';
  }
}
