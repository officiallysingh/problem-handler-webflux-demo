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

  @Schema(
      description = "Goods and Services Tax Identification Number (GSTIN) for State",
      example = "06AAACD1977A1Z3")
  private String gstin;

  @Schema(description = "Harmonized System of Nomenclature (HSN) Code for State", example = "9971")
  private String hsnCode;

  @Schema(description = "Whether its a Union Territory", nullable = true, example = "false")
  private Boolean isUT = false;

  @Schema(description = "Nature of Service", example = "Other Financial and related services")
  private String natureOfService;

  public static StateResponse of(final State state) {
    return new StateResponse(state.id(),
        state.code(),
        state.name(),
        state.gstCode(),
        state.gstin(),
        state.hsnCode(),
        state.isUT(),
        state.natureOfService());
  }
}
