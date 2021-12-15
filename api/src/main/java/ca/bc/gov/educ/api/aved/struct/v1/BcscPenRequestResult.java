package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Pen request result.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BcscPenRequestResult extends PenRequestResult{

  /**
   * The DID.
   */
  String did;
}
