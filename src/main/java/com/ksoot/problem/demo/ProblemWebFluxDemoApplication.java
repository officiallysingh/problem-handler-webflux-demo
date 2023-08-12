package com.ksoot.problem.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@EnableR2dbcRepositories(value = {"com.ksoot.problem.demo.repository"})
@EnableReactiveMongoRepositories(value = {"com.ksoot.problem.demo.repository"})
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication
public class ProblemWebFluxDemoApplication {

  public static void main(final String[] args) {
    SpringApplication.run(ProblemWebFluxDemoApplication.class, args);
  }

}
