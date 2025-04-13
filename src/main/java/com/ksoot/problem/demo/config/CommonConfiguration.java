package com.ksoot.problem.demo.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.HttpWebHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

@Configuration
@EnableConfigurationProperties(
    value = {TaskExecutionProperties.class, TaskSchedulingProperties.class})
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class CommonConfiguration {

//  public CommonConfiguration(final LocaleContextResolver localeContextResolver) {
//    if(localeContextResolver instanceof AcceptHeaderLocaleContextResolver localeCtxResolver) {
//      localeCtxResolver
//          .setSupportedLocales(List.of(
//              Locale.ENGLISH,
//              Locale.FRENCH
//          ));
//      localeCtxResolver.setDefaultLocale(Locale.ENGLISH); // Optional: set default locale
//    }
//    System.out.println("?????");
//  }

  @ConditionalOnMissingBean(value = ApplicationEventMulticaster.class)
  @Bean
  public ApplicationEventMulticaster applicationEventMulticaster(
      @Qualifier("applicationTaskExecutor") final Executor taskExecutor) {
    SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
    eventMulticaster.setTaskExecutor(taskExecutor);
    return eventMulticaster;
  }

  @ConditionalOnMissingBean(value = SpringProfiles.class)
  @Bean
  public SpringProfiles springProfiles(final Environment environment) {
    return new SpringProfiles(environment);
  }

  @Bean
  Validator validator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Bean
  ValidatingMongoEventListener validatingMongoEventListener(
      final LocalValidatorFactoryBean factory) {
    return new ValidatingMongoEventListener(factory);
  }

  @Bean
  LocalValidatorFactoryBean validatorFactory() {
    final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
    return validator;
  }
}
