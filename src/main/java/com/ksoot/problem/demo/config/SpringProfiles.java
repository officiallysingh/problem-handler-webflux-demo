package com.ksoot.problem.demo.config;

import lombok.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @author Rajveer Singh */
public class SpringProfiles {

  public static final String LOCAL = "local";

  public static final String DOCKER = "docker";

  public static final String DEVELOPMENT = "dev";

  public static final String PREPROD = "preprod";

  public static final String PRODUCTION = "prod";

  public static final String TEST = "test";

  public static final String NOT_TEST = "!test";

  @SuppressWarnings("unused")
  private static final String SPRING_DEFAULT_PROFILE_KEY = "spring.profiles.default";

  // private static final String DEFAULT_PROFILE = "local";

  private static final String FALLBACK_DEFAULT_PROFILE = "default";

  private static List<String> ACTIVE_PROFILES;

  @NonNull private static Environment environment;

  SpringProfiles(final Environment environment) {
    SpringProfiles.environment = environment;
  }

  /**
   * Set a default to use when no profile is configured.
   *
   * @param app the Spring application
   */
  public static void addDefaultProfile(final SpringApplication app) {
    Map<String, Object> defaultProfiles = new HashMap<>(1);
    // defaultProfiles.put(SPRING_DEFAULT_PROFILE_KEY, DEFAULT_PROFILE);
    app.setDefaultProperties(defaultProfiles);
  }

  public static void addAdditionalProfiles(
      final SpringApplication app, final String[] additionalProfiles) {
    app.setAdditionalProfiles(additionalProfiles);
  }

  /**
   * Get the profiles that are applied else get default profiles.
   *
   * @return profiles
   */
  public static List<String> getActiveProfiles() {
    if (ACTIVE_PROFILES == null) {
      if (environment.getActiveProfiles().length == 0) {
        ACTIVE_PROFILES = Arrays.asList(environment.getDefaultProfiles());
        return ACTIVE_PROFILES;
      } else {
        ACTIVE_PROFILES = Arrays.asList(environment.getActiveProfiles());
        return ACTIVE_PROFILES;
      }
    } else {
      return ACTIVE_PROFILES;
    }
  }

  public static boolean isLocal() {
    return getActiveProfiles().contains(LOCAL);
  }

  public static boolean isDev() {
    return getActiveProfiles().contains(DEVELOPMENT);
  }

  public static boolean isPreprod() {
    return getActiveProfiles().contains(PREPROD);
  }

  public static boolean isProd() {
    return getActiveProfiles().contains(PRODUCTION);
  }

  public static boolean isEnabled(final String profile) {
    return getActiveProfiles().contains(profile);
  }

  public static Set<String> exclusiveProfiles() {
    return Set.of(FALLBACK_DEFAULT_PROFILE, LOCAL, DOCKER, DEVELOPMENT, PREPROD, PRODUCTION, TEST);
  }
}
