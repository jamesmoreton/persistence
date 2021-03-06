package com.jamesmoreton;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class EmployeeFactory {

  private static final List<String> FIRST_NAMES = List.of("Alice", "Bob", "Charlotte", "Dave");
  private static final List<String> LAST_NAMES = List.of("Arno", "Bonnie", "Chewie", "Dobby");
  private static final LocalDate BEGINNING_OF_PAST = LocalDate.of(2000, 1, 1);
  private static final int DEFAULT_PAST_WINDOW = 3650;

  public static Employee create() {
    return new Employee(
        UUID.randomUUID(),
        selectRandom(FIRST_NAMES),
        selectRandom(LAST_NAMES),
        onRandomDayInThePast(),
        selectRandom(List.of(Employee.Role.values()))
    );
  }

  private static <T> T selectRandom(List<T> list) {
    return list.get(ThreadLocalRandom.current().nextInt(list.size()));
  }

  private static LocalDate onRandomDayInThePast() {
    return BEGINNING_OF_PAST.minusDays(ThreadLocalRandom.current().nextInt(DEFAULT_PAST_WINDOW));
  }
}
