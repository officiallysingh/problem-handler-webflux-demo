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
}
