package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvedStudent {
  /**
   * The Mincode.
   */
  String mincode;
  /**
   * The Pen.
   */
  String pen;
  /**
   * The Local id.
   */
  String localID;
  /**
   * The Legal surname.
   */
  String legalSurname;
  /**
   * The Legal given name.
   */
  String legalGivenName;
  /**
   * The Legal middle names.
   */
  String legalMiddleNames;
  /**
   * The Usual surname.
   */
  String usualSurname;
  /**
   * The Usual given name.
   */
  String usualGivenName;
  /**
   * The Usual middle names.
   */
  String usualMiddleNames;
  /**
   * The Birth date.
   */
  String birthDate;
  /**
   * The Gender.
   */
  String gender;
  /**
   * The Enrolled grade code.
   */
  String enrolledGradeCode;
  /**
   * The Postal code.
   */
  String postalCode;
}
