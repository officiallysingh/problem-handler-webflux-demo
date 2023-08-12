package com.ksoot.problem.demo.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.ksoot.problem.demo.util.AppConstants.GST_STATE_CODE;
import static com.ksoot.problem.demo.util.AppConstants.REGEX_ALPHABETS_AND_SPACES;
import static com.ksoot.problem.demo.util.AppConstants.REGEX_GSTIN;
import static com.ksoot.problem.demo.util.AppConstants.REGEX_HSN_CODE;
import static com.ksoot.problem.demo.util.AppConstants.REGEX_STATE_CODE;

@Getter
@Accessors(chain = true, fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@PersistenceCreator))
@Document(collection = "states")
@CompoundIndexes({
    @CompoundIndex(
        name = "unqIdxStatesCodeName",
        unique = true,
        background = true,
        def = "{'code' : 1, 'name': 1}")
})
public class State {

  @Id
  private String id;

  @Version
  private Long version;

  @NotEmpty
  @Pattern(regexp = REGEX_STATE_CODE)
  @Indexed(name = "idxStatesCode", background = true, unique = true)
  @Field(name = "code")
  private String code;

  @NotEmpty
  @Size(max = 50)
  @Pattern(regexp = REGEX_ALPHABETS_AND_SPACES)
  @Setter
  @Field(name = "name")
  private String name;

  @NotEmpty
  @Pattern(regexp = GST_STATE_CODE)
  @Setter
  @Field(name = "gstCode")
  private String gstCode;

  @NotEmpty
  @Pattern(regexp = REGEX_GSTIN)
  @Setter
  @Field(name = "gstin")
  private String gstin;

  @NotEmpty
  @Pattern(regexp = REGEX_HSN_CODE)
  @Setter
  @Field(name = "hsnCode")
  private String hsnCode;

  @NotNull
  @Setter
  @Field(name = "isUT")
  private Boolean isUT;

  @NotEmpty
  @Size(max = 256)
  @Setter
  @Field(name = "natureOfService")
  private String natureOfService;

  public static State of(String code, String name, String gstCode, String gstin,
                         String hsnCode, Boolean isUT, String natureOfService) {
    return new State(null, null, code, name, gstCode, gstin, hsnCode, isUT, natureOfService);
  }
}
