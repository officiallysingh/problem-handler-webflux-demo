package com.ksoot.problem.demo.config;

import com.ksoot.problem.demo.util.ClassUtils;
import com.ksoot.problem.demo.util.CommonConstant;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.WebApplicationType;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AppInitializer {

  static void initialize(final Environment env, final WebApplicationType webApplicationType) {
    //    TimeZone.setDefault(DEFAULT_TIME_ZONE);
    //    log.info("Set Default time zone: " + DEFAULT_TIME_ZONE.getID());
    log.info("System time zone: " + CommonConstant.DEFAULT_ZONE_ID);
    validateProfiles(env);
    logApplicationStartup(webApplicationType, env);
  }

  private static void validateProfiles(final Environment env) {
    Set<String> activeProfiles = Sets.newHashSet(env.getActiveProfiles());
    Set<String> defaultProfiles = Sets.newHashSet(env.getDefaultProfiles());
    Set<String> exclusiveProfiles = SpringProfiles.exclusiveProfiles();
    SetView<String> intersection =
        Sets.intersection(
            exclusiveProfiles, activeProfiles.isEmpty() ? defaultProfiles : activeProfiles);

    if (intersection.isEmpty()) {
      log.error(
          "You have misconfigured your application! It should run "
              + "with exactly one of active profiles: "
              + exclusiveProfiles
              + ", depending on environment.");
    } else if (intersection.size() > 1) {
      log.error(
          "You have misconfigured your application! It should not run with active profiles "
              + intersection
              + " togeather. Exactly one of profiles: "
              + exclusiveProfiles
              + " must be active, depending on environment.");
    }

    // new MutuallyExclusiveConfigurationPropertiesException(exclusiveProfiles, intersection)
  }

  private static void logApplicationStartup(
      final WebApplicationType webApplicationType, Environment env) {
    String protocol = "http";
    if (env.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    String serverPort =
        env.getProperty("server.port") == null ? "" + 8080 : env.getProperty("server.port");
    String contextPath =
        webApplicationType == WebApplicationType.REACTIVE
            ? env.getProperty("spring.webflux.base-path")
            : env.getProperty("server.servlet.context-path");

    if (StringUtils.isBlank(contextPath)) {
      contextPath = "";
    } else {
      contextPath = contextPath.startsWith(CommonConstant.SLASH) ? contextPath : CommonConstant.SLASH + contextPath;
    }
    String hostAddress = "localhost";
    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.warn("The host name could not be determined, using 'localhost' as fallback");
    }
    String activeProfile = env.getProperty("spring.profiles.active");
    String profiles =
        StringUtils.isEmpty(activeProfile)
            ? Arrays.stream(env.getDefaultProfiles()).collect(Collectors.joining(", "))
            : activeProfile;

    String applicationInfo =
        String.format(
            "Application '%1$s' of type: '%2$s' is running! %n\t",
            env.getProperty("spring.application.name"), webApplicationType);
    String localUrl =
        String.format(
            "Access Local: \t\t\t%1$s://localhost:%2$s%3$s/swagger-ui.html%n\t",
            protocol, serverPort, contextPath);
    String externalUrl =
        String.format(
            "Access External: \t\t%1$s://%2$s:%3$s%4$s/swagger-ui.html%n\t",
            protocol, hostAddress, serverPort, contextPath);
    String activeProfiles = String.format("Profiles: \t\t\t\t%1$s%n\t", profiles);

    boolean isProblemEnabled = env.getProperty("application.problem.enabled", Boolean.class, false);
    boolean isProblemWeb = isProblemEnabled && webApplicationType == WebApplicationType.SERVLET;
    boolean problemHandling =
        isProblemWeb
            ? ClassUtils.doesClassExist(
                "com.pchf.common.problem.spring.advice.web.ProblemHandlingWeb")
            : ClassUtils.doesClassExist(
                "com.pchf.common.problem.spring.advice.webflux.ProblemHandlingWebflux");

    boolean security =
        env.getProperty("application.security.enabled", Boolean.class, false)
            && ClassUtils.doesClassExist(
                "org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration");

    String securityFeature = security ? "security" : "";
    String problemFeature = problemHandling ? "problem-handling" : "";

    String enabledConfigs =
        Arrays.asList(securityFeature, problemFeature).stream()
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining(", "));

    String enabledConfigurations = String.format("Enabled Configurations: %1$s", enabledConfigs);

    String logInfo =
        applicationInfo + localUrl + externalUrl + activeProfiles + enabledConfigurations;
    log.info(
        "\n-------------------------------------------------------------------------------------------\n\t"
            + logInfo
            + "\n-------------------------------------------------------------------------------------------");
  }
}
