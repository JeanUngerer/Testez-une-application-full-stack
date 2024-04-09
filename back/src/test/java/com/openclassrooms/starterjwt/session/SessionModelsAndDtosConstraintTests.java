package com.openclassrooms.starterjwt.session;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class SessionModelsAndDtosConstraintTests {
    private LocalValidatorFactoryBean localValidatorFactory = new LocalValidatorFactoryBean();

    @Test
    public void sessionDtoConstraintsTest() {
        SessionDto sessionDto = new SessionDto();
        // initialize sessionDTO fields
        sessionDto.setId(1L);
        sessionDto.setName("Test Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("This is a test session");
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        validateSession(sessionDto);
    }

    @Test
    public void sessionDtoConstraintsTest_Negative() {
        SessionDto sessionDto = new SessionDto();
        // initialize sessionDTO fields with invalid values
        sessionDto.setId(1L);
        sessionDto.setName("");
        sessionDto.setDate(null);
        sessionDto.setTeacher_id(null);
        sessionDto.setDescription("desc");
        sessionDto.setUsers(null);
        validateSessionNegative(sessionDto, 3);
    }

    @Test
    public void sessionModelConstraintsTest() {
        Session session = new Session();
        // initialize session fields
        Teacher teacher = new Teacher(1L, "Doe", "John", null, null);
        User user1 = new User(1L, "example1@example.com", "Doe", "John", "123456", true, null, null);
        User user2 = new User(2L, "example2@example.com", "Smith", "Adam", "123456", false, null, null);
        List<User> users = Arrays.asList(user1, user2);

        session.setId(1L);
        session.setName("Test Session");
        session.setDate(new java.util.Date());
        session.setDescription("This is a test session");
        session.setTeacher(teacher);
        session.setUsers(users);

        validateSession(session);
    }

    private <T> void validateSession(T object) {
        localValidatorFactory.afterPropertiesSet();
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactory.validate(object);
        System.out.println(constraintViolations);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void sessionModelConstraintsTest_Negative() {
        Session session = new Session();
        // initialize session fields with invalid values
        session.setId(null);
        session.setName("");
        session.setDate(null);
        session.setDescription("descc");
        session.setTeacher(null);
        session.setUsers(null);
        validateSessionNegative(session, 2);
    }

    private <T> void validateSessionNegative(T object, Integer violationNumber) {
        localValidatorFactory.afterPropertiesSet();
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactory.validate(object);
        System.out.println(constraintViolations);
        assertEquals(violationNumber, constraintViolations.size());
    }
}