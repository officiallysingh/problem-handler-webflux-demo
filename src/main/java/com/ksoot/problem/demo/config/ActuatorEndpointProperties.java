package com.ksoot.problem.demo.config;

import com.ksoot.problem.demo.util.CommonConstant;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@ToString
@ConfigurationProperties(
    prefix = "management.endpoints.web",
    ignoreInvalidFields = true,
    ignoreUnknownFields = true)
@Valid
public class ActuatorEndpointProperties {

  private String basePath = "/actuator";

  private Exposure exposure = new Exposure();

  private Map<String, String> pathMapping = Collections.emptyMap();

  static final List<String> ALL_ENDPOINTS =
      Arrays.asList(
          "auditevents",
          "beans",
          "caches",
          "conditions",
          "configprops",
          "env",
          "flyway",
          "health",
          "heapdump",
          "httptrace",
          "info",
          "integrationgraph",
          "liquibase",
          "logfile",
          "loggers",
          "mappings",
          "metrics",
          "prometheus",
          "scheduledtasks",
          "sessions",
          "shutdown",
          "threaddump");

  public String[] getPaths() {
    List<String> exposedEndpoints = null;
    if (this.exposure.getInclude().contains("*")) {
      exposedEndpoints = ActuatorEndpointProperties.ALL_ENDPOINTS;
    } else {
      exposedEndpoints = this.exposure.getInclude();
    }

    if (CommonConstant.SLASH.equals(this.basePath)) {
      return exposedEndpoints.stream()
          .map(path -> CommonConstant.SLASH + this.pathMapping.getOrDefault(path, path) + "/**")
          .toArray(String[]::new);
    } else {
      return new String[] {this.basePath + "/**"};
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @ToString
  @Valid
  public static class Exposure {

    private List<String> include = ALL_ENDPOINTS;
  }
}
