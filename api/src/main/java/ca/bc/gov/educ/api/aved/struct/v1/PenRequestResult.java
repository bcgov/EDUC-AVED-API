package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Pen request result.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PenRequestResult {

  /**
   * The Pen.
   */
  String pen;
}
