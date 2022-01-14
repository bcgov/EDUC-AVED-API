package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The type Request.
 */
@Data
public class PenRequest extends Request {

  /**
   * The Legal surname.
   */
  @Size(min = 1, max = 255)
  @NotBlank
  String legalSurname;
  /**
   * The Legal given name.
   */
  @Size(min = 1, max = 255)
  String legalGivenName;
  /**
   * The Legal middle name.
   */
  @Size(min = 1, max = 255)
  String legalMiddleName;

  /**
   * The Postal code.
   */
  @Size(max = 6, min = 6)
  @Pattern(regexp = "^([A-Z]\\d[A-Z]\\d[A-Z]\\d|)$")
  String postalCode;
}
