package com.ksoot.problem.demo.repository;

import com.ksoot.problem.demo.model.Employee;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface EmployeeRepository extends R2dbcRepository<Employee, Long> {
}
