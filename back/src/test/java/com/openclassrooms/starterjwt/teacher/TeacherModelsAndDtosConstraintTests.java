package com.openclassrooms.starterjwt.teacher;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class TeacherModelsAndDtosConstraintTests {

    private LocalValidatorFactoryBean localValidatorFactory = new LocalValidatorFactoryBean();

    @Test
    public void teacherDtoConstraintsTest() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("john");
        teacherDto.setFirstName("doe");
        teacherDto.setCreatedAt(null);
        teacherDto.setUpdatedAt(null);
        validateTeacherDto(teacherDto);
    }

    private <T> void validateTeacherDto(T object) {
        localValidatorFactory.afterPropertiesSet();
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactory.validate(object);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void teacherDtoConstraintsTest_Negative() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);

        validateTeacherDtoNegative(teacherDto, 2);
    }

    private <T> void validateTeacherDtoNegative(T object, Integer violationNumber) {
        localValidatorFactory.afterPropertiesSet();
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactory.validate(object);
        assertEquals(violationNumber, constraintViolations.size());
    }

    @Test
    public void teacherModelConstraintsTest() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("john");
        teacher.setFirstName("doe");
        teacher.setUpdatedAt(null);
        validateTeacherModel(teacher);
    }

    private <T> void validateTeacherModel(T object) {
        localValidatorFactory.afterPropertiesSet();
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactory.validate(object);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void teacherModelConstraintsTest_Negative() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUpdatedAt(LocalDateTime.now());
        validateTeacherModelNegative(teacher, 2);
    }

    private <T> void validateTeacherModelNegative(T object, Integer violationNumber) {
        localValidatorFactory.afterPropertiesSet();
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactory.validate(object);
        assertEquals(violationNumber, constraintViolations.size());
    }
}