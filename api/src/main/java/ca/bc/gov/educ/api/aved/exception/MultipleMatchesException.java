package ca.bc.gov.educ.api.aved.exception;

import ca.bc.gov.educ.api.aved.struct.v1.soam.SoamLoginEntity;
import lombok.val;

import java.util.Map;

/**
 * The type Invalid value exception.
 */
public class MultipleMatchesException extends RuntimeException {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 8926815015510650437L;

  private SoamLoginEntity soamLoginEntity;

  /**
   * Instantiates a new Invalid value exception.
   *
   * @param paramsMap the params map
   */
  public MultipleMatchesException(String... paramsMap) {
    super(MultipleMatchesException.generateMessage(
      ExceptionUtils.toMap(String.class, String.class, (Object[]) paramsMap)));
  }

  public MultipleMatchesException(SoamLoginEntity soamLoginEntity) {
    this.soamLoginEntity = soamLoginEntity;
  }

  public SoamLoginEntity getSoamLoginEntity() {
    return soamLoginEntity;
  }

  public void setSoamLoginEntity(SoamLoginEntity soamLoginEntity) {
    this.soamLoginEntity = soamLoginEntity;
  }

  /**
   * Generate message string.
   *
   * @param values the values
   * @return the string
   */
  private static String generateMessage(Map<String, String> values) {
    val message = "Invalid request parameters provided: ";
    return message + values;
  }
}
