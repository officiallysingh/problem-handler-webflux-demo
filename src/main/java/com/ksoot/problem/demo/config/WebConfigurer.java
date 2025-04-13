package com.ksoot.problem.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveMultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.HttpWebHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

/**
 * @author Rajveer Singh
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ReactiveMultipartProperties.class)
@AutoConfigureAfter(value = {CommonConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableWebFlux
public class WebConfigurer implements WebFluxConfigurer {

  private final ObjectMapper objectMapper;

  private final Environment env;

  private final ReactiveMultipartProperties multipartProperties;

  public WebConfigurer(final ObjectMapper objectMapper, final Environment env,
                       final ReactiveMultipartProperties multipartProperties) {
    this.objectMapper = objectMapper;
    this.env = env;
    this.multipartProperties = multipartProperties;
  }

  @PostConstruct
  public void onStartup() {
    if (this.env.getActiveProfiles().length != 0) {
      log.info("Web application configuration, using profiles: {}", (Object[]) this.env.getActiveProfiles());
    }

    // App initialization and logging startup info
    AppInitializer.initialize(this.env, WebApplicationType.REACTIVE);

    log.info("Web application fully configured");
  }

  @Override
  public void configureHttpMessageCodecs(final ServerCodecConfigurer configurer) {
    configurer.defaultCodecs().jackson2JsonEncoder(
        new Jackson2JsonEncoder(this.objectMapper)
    );

    configurer.defaultCodecs().jackson2JsonDecoder(
        new Jackson2JsonDecoder(this.objectMapper)
    );

    configurer.defaultCodecs().configureDefaultCodec((codec) -> {
      if (codec instanceof MultipartHttpMessageReader) {
        HttpMessageReader<Part> partReader = ((MultipartHttpMessageReader) codec).getPartReader();
        if (partReader instanceof DefaultPartHttpMessageReader partHttpMessageReader) {
          partHttpMessageReader.setMaxInMemorySize((int) this.multipartProperties.getMaxInMemorySize().toBytes());
          partHttpMessageReader.setMaxDiskUsagePerPart(this.multipartProperties.getMaxDiskUsagePerPart().toBytes());
          partHttpMessageReader.setMaxHeadersSize((int) this.multipartProperties.getMaxHeadersSize().toBytes());
          partHttpMessageReader.setMaxParts(this.multipartProperties.getMaxParts());
        }
      }
    });
  }

//	private final ObjectProvider<HandlerMethodArgumentResolver> argumentResolvers;
//	this.argumentResolvers.orderedStream().forEach(configurer::addCustomResolver);


  @Bean
  public HttpHandler httpHandler(ApplicationContext applicationContext) {
    LocaleContextHolder.getLocaleContext().getLocale();
    HttpHandler delegate = WebHttpHandlerBuilder
            .applicationContext(applicationContext).build();
    return new HttpWebHandlerAdapter(((HttpWebHandlerAdapter) delegate)) {
      @Override
      protected ServerWebExchange createExchange(ServerHttpRequest request,
                                                 ServerHttpResponse response) {
        ServerWebExchange serverWebExchange = super
                .createExchange(request, response);
        LocaleContext localeContext = serverWebExchange.getLocaleContext();
        if (localeContext != null) {
          LocaleContextHolder.setLocaleContext(localeContext, true);
//          LocaleContextHolder.setLocaleContext(localeContext);
        }
        return serverWebExchange;
      }
    };
  }
}
