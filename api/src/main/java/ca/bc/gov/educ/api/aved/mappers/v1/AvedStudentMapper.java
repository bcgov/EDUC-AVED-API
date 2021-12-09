package ca.bc.gov.educ.api.aved.mappers.v1;

import ca.bc.gov.educ.api.aved.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.aved.mappers.UUIDMapper;
import ca.bc.gov.educ.api.aved.struct.v1.AvedStudent;
import ca.bc.gov.educ.api.aved.struct.v1.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
public interface AvedStudentMapper {
  AvedStudentMapper mapper = Mappers.getMapper(AvedStudentMapper.class);

  @Mapping(target = "usualSurname", source = "usualLastName")
  @Mapping(target = "usualGivenName", source = "usualFirstName")
  @Mapping(target = "legalSurname", source = "legalLastName")
  @Mapping(target = "legalGivenName", source = "legalFirstName")
  @Mapping(target = "gender", source = "genderCode")
  @Mapping(target = "enrolledGradeCode", source = "gradeCode")
  @Mapping(target = "birthDate", expression = "java(org.apache.commons.lang3.RegExUtils.removeAll(student.getDob(), \"[^\\\\d]\" ) )")
  AvedStudent toAvedStudent(Student student);
}
