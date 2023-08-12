package com.ksoot.problem.demo.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import java.time.LocalDate;

import static com.ksoot.problem.demo.util.AppConstants.REGEX_ALPHABETS_AND_SPACES;

@Getter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@PersistenceCreator))
public class Employee {

  @Id
  private Long id;

  @NotEmpty
  @Size(max = 50)
  @Pattern(regexp = REGEX_ALPHABETS_AND_SPACES)
  private String name;

  @NotNull
  @Past
  private LocalDate dob;

  public static Employee of(final String name, final LocalDate dob) {
    Employee employee = new Employee();
    employee.name = name;
    employee.dob = dob;
    return employee;
  }
}
