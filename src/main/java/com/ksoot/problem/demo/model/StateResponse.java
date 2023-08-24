package com.ksoot.problem.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateResponse {

  @Schema(description = "Internal record id", example = "646a105f5f325c211abed0c9")
  private String id;

  @Schema(description = "State code. Two char code like HR for Haryana", example = "HR")
  private String code;

  @Schema(description = "State name", example = "Haryana")
  private String name;

  @Schema(description = "State GST Code, like 6 for Haryana", example = "6")
  private String gstCode;

  public static StateResponse of(final State state) {
    return new StateResponse(state.id(),
        state.code(),
        state.name(),
        state.gstCode());
  }
}
