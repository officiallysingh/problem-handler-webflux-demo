package com.ksoot.problem.demo.repository;

import com.ksoot.problem.demo.model.State;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface StateRepository extends ReactiveMongoRepository<State, String> {

  @Query("{'$or':[ { 'id' : ?0 }, { 'code' : ?0 } ] }")
  Mono<State> findByIdOrCode(final String idOrCode);
}
