package ca.bc.gov.educ.api.aved.struct.v1.soam;

import lombok.Data;

import java.util.UUID;


@Data
public class SoamServicesCard {
  UUID servicesCardInfoID;
  UUID digitalIdentityID;
  String did;
  String userDisplayName;
  String givenName;
  String givenNames;
  String surname;
  String birthDate;
  String gender;
  String email;
  String streetAddress;
  String city;
  String province;  
  String country;
  String identityAssuranceLevel; 
  String postalCode;   
  String createUser;
  String createDate;
  String updateUser;
  String updateDate;
}
