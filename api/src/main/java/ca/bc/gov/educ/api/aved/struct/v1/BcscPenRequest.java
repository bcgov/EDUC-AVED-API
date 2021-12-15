package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * The type Request.
 */
@Data
public class BcscPenRequest extends Request {

  /**
   * The DID.
   */
  @Size(max = 30)
  @NotBlank
  String did;
  /**
   * The user display name.
   */
  @Size(max = 255)
  @NotBlank
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
  @NotBlank
  String surname;
  /**
   * The identity assurance level.
   */
  @Size(max = 1)
  @NotBlank
  String identityAssuranceLevel;
  /**
   * The email.
   */
  @Size(max = 255)
  @Email(message = "Email must be valid email address.")
  String email;

}
