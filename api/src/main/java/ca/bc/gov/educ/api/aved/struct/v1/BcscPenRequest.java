package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The type Request.
 */
@Data
public class BcscPenRequest extends Request {

  /**
   * The DID.
   */
  @Size(min = 1, max = 255)
  @NotBlank(message = "did cannot be null")
  @Pattern(regexp = "^[a-zA-Z0-9]+$")
  String did;
  /**
   * The user display name.
   */
  @Size(min = 1, max = 255)
  @NotBlank(message = "userDisplayName cannot be null")
  String userDisplayName;
  /**
   * The given name.
   */
  @Size(min = 1, max = 255)
  String givenName;
  /**
   * The given names.
   */
  @Size(min = 1, max = 255)
  String givenNames;
  /**
   * The surname.
   */
  @Size(min = 1, max = 255)
  @NotBlank(message = "surname cannot be null")
  String surname;
  /**
   * The identity assurance level.
   */
  @Size(min = 1, max = 1)
  @NotBlank(message = "identityAssuranceLevel cannot be null")
  String identityAssuranceLevel;
  /**
   * The email.
   */
  @Size(min = 1, max = 255)
  @Email(message = "Email must be valid email address.")
  String email;

}
