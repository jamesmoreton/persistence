package com.jamesmoreton.postgres;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jamesmoreton.EmployeePersistenceService;
import com.jamesmoreton.EmployeeService;

public class PostgresMainline {

  public static class PostgresModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(EmployeePersistenceService.class).to(PostgresEmployeePersistenceService.class);
    }
  }

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new PostgresModule());
    EmployeeService employeeService = injector.getInstance(EmployeeService.class);
    employeeService.doThings();
  }
}
