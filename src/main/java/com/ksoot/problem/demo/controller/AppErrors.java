package com.ksoot.problem.demo.controller;

import com.ksoot.problem.core.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppErrors implements ErrorType {

  REMOTE_HOST_NOT_AVAILABLE("remote.host.not.available",
      "Looks like something wrong with remote host: {0}", HttpStatus.SERVICE_UNAVAILABLE);

  private final String errorKey;

  private final String defaultDetail;

  private final HttpStatus status;

  AppErrors(final String errorKey, final String defaultDetail, final HttpStatus status) {
    this.errorKey = errorKey;
    this.defaultDetail = defaultDetail;
    this.status = status;
  }
}
