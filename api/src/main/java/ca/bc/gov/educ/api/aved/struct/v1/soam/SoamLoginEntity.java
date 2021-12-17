package ca.bc.gov.educ.api.aved.struct.v1.soam;

import lombok.Data;

import java.util.UUID;

@Data
public class SoamLoginEntity {

	private SoamStudent student;
	private SoamServicesCard serviceCard;
	private UUID digitalIdentityID;
}
