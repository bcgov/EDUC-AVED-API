package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Request.
 */
@Data
public class BcscPenRequest extends Request {

  /**
   * The DID.
   */
  @Size(max = 255)
  @NotNull(message = "did cannot be null")
  String did;
  /**
   * The user display name.
   */
  @Size(max = 255)
  @NotNull(message = "userDisplayName cannot be null")
  String userDisplayName;
  /**
   * The given name.
   */
  @Size(max = 255)
  String givenName;
  /**
   * The given names.
   */
  @Size(max = 255)
  String givenNames;
  /**
   * The surname.
   */
  @Size(max = 255)
  @NotNull(message = "surname cannot be null")
  String surname;
  /**
   * The identity assurance level.
   */
  @Size(max = 1)
  @NotNull(message = "identityAssuranceLevel cannot be null")
  String identityAssuranceLevel;
  /**
   * The email.
   */
  @Size(max = 255)
  @Email(message = "Email must be valid email address.")
  String email;

}
