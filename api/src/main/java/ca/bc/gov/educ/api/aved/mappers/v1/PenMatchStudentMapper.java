package ca.bc.gov.educ.api.aved.mappers.v1;

import ca.bc.gov.educ.api.aved.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.aved.mappers.UUIDMapper;
import ca.bc.gov.educ.api.aved.struct.v1.PenRequest;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationRequest;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
public interface PenMatchStudentMapper {
  PenMatchStudentMapper mapper = Mappers.getMapper(PenMatchStudentMapper.class);

  @Mapping(target = "pen", source = "pen")
  @Mapping(target = "surname", source = "legalSurname")
  @Mapping(target = "givenName", source = "legalGivenName")
  @Mapping(target = "middleName", source = "legalMiddleName")
  @Mapping(target = "sex", source = "gender")
  @Mapping(target = "dob", source = "birthDate")
  @Mapping(target = "postal", source = "postalCode")
  PenMatchStudent toPenMatchStudent(PenValidationRequest validationRequest);

  @Mapping(target = "surname", source = "legalSurname")
  @Mapping(target = "givenName", source = "legalGivenName")
  @Mapping(target = "middleName", source = "legalMiddleName")
  @Mapping(target = "sex", source = "gender")
  @Mapping(target = "dob", source = "birthDate")
  @Mapping(target = "postal", source = "postalCode")
  PenMatchStudent toPenMatchStudent(PenRequest penRequest);

}
