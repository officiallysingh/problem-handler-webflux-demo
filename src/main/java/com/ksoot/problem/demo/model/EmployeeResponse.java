package com.ksoot.problem.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeResponse {

  @Schema(description = "Internal record id", example = "1")
  private Long id;

  @Schema(description = "Employee name", example = "John Rambo")
  private String name;

  @Schema(description = "Employee date of birth", example = "1983-06-06")
  private LocalDate dob;

  public static EmployeeResponse of(final Employee employee) {
    return new EmployeeResponse(employee.id(), employee.name(), employee.dob());
  }
}
