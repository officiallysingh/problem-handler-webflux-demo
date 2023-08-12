package com.ksoot.problem.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.ksoot.problem.demo.util.AppConstants.GST_STATE_CODE;
import static com.ksoot.problem.demo.util.AppConstants.REGEX_ALPHABETS_AND_SPACES;
import static com.ksoot.problem.demo.util.AppConstants.REGEX_GSTIN;
import static com.ksoot.problem.demo.util.AppConstants.REGEX_HSN_CODE;
import static com.ksoot.problem.demo.util.AppConstants.REGEX_STATE_CODE;

@Getter
@Setter
public class CreateStateRequest {

  @NotEmpty
  @Size(max = 50)
  @Pattern(regexp = REGEX_STATE_CODE)
  @Schema(description = "State code. Two char code like HR for Haryana", example = "HR")
  private String code;

  @NotEmpty
  @Size(max = 50)
  @Pattern(regexp = REGEX_ALPHABETS_AND_SPACES)
  @Schema(description = "State name", example = "Haryana")
  private String name;

  @NotNull
  @Pattern(regexp = GST_STATE_CODE)
  @Schema(description = "State GST Code, like 6 for Haryana", example = "6")
  private String gstCode;

  @NotEmpty
  @Pattern(regexp = REGEX_GSTIN)
  @Schema(
      description = "Goods and Services Tax Identification Number (GSTIN) for State",
      example = "06AAACD1977A1Z3")
  private String gstin;

  @NotEmpty
  @Pattern(regexp = REGEX_HSN_CODE)
  @Schema(description = "Harmonized System of Nomenclature (HSN) Code for State", example = "9971")
  private String hsnCode;

  @Schema(description = "Whether its a Union Territory", nullable = true, example = "false")
  private boolean isUT;

  @NotEmpty
  @Size(max = 256)
  @Schema(description = "Nature of Service", example = "Other Financial and related services")
  private String natureOfService;
}
