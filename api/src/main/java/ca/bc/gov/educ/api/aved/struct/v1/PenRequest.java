package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Request.
 */
@Data
public class PenRequest extends Request {

  /**
   * The Legal surname.
   */
  @Size(max = 25)
  @NotNull
  @NotBlank
  String legalSurname;
  /**
   * The Legal given name.
   */
  @Size(max = 25)
  @NotBlank
  String legalGivenName;
  /**
   * The Legal middle name.
   */
  @Size(max = 25)
  @NotBlank
  String legalMiddleName;

}
