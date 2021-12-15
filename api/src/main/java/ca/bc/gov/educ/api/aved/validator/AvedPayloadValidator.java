package ca.bc.gov.educ.api.aved.validator;

import ca.bc.gov.educ.api.aved.struct.v1.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Pen my ed payload validator.
 */
@Component
@Slf4j
public class AvedPayloadValidator {
  public static final String PEN_LIST = "Pen List";

  /**
   * The Dob format.
   */
  private final DateTimeFormatter dobFormat = DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);

  /**
   * Validate pen request payload list.
   *
   * @param request the request
   * @return the list
   */
  public List<FieldError> validatePenRequestPayload(final Request request) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    try {
      LocalDate.parse(request.getBirthDate(), dobFormat);
    } catch (final DateTimeParseException e) {
      apiValidationErrors.add(createFieldError("birthdate", request.getBirthDate(), "invalid birth date , must be yyyyMMdd."));
    }
    return apiValidationErrors;
  }

  /**
   * Create field error field error.
   *
   * @param fieldName     the field name
   * @param rejectedValue the rejected value
   * @param message       the message
   * @return the field error
   */
  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("penRequest", fieldName, rejectedValue, false, null, null, message);
  }

}
