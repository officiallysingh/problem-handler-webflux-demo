package com.ksoot.problem.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

@EnableTransactionManagement
class TransactionConfiguration {

  @Bean
  TransactionalOperator transactionalOperator(ReactiveTransactionManager txm) {
    return TransactionalOperator.create(txm);
  }

  @Bean
  ReactiveTransactionManager reactiveMongoTransactionManager(ReactiveMongoDatabaseFactory rdf) {
    return new ReactiveMongoTransactionManager(rdf);
  }
}
