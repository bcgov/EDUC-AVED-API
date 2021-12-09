package ca.bc.gov.educ.api.aved.exception;

/**
 * The type My ed api runtime exception.
 */
public class AvedAPIRuntimeException extends RuntimeException {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 5241655513745148898L;

  /**
   * Instantiates a new My ed api runtime exception.
   *
   * @param message the message
   */
  public AvedAPIRuntimeException(String message) {
		super(message);
	}

  /**
   * Instantiates a new My ed api runtime exception.
   *
   * @param exception the exception
   */
  public AvedAPIRuntimeException(Throwable exception) {
    super(exception);
  }

}
