package com.openclassrooms.starterjwt.user;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
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
public class UserModelsAndDtosConstraintTests {

    private LocalValidatorFactoryBean localValidatorFactory = new LocalValidatorFactoryBean();

    @Test
    public void userDtoConstraintsTest() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setLastName("john");
        userDto.setFirstName("doe");
        userDto.setEmail("email@Atest.com");
        userDto.setCreatedAt(null);
        userDto.setUpdatedAt(null);
        validateUser(userDto);
    }



    @Test
    public void userDtoConstraintsTest_Negative() {
        UserDto userDto = new UserDto();
        userDto.setEmail("invalidEmail");
        userDto.setId(1L);
        validateUserNegative(userDto, 1);
    }


    @Test
    public void userModelConstraintsTest() {
        User user = new User();
        user.setId(1L);
        user.setLastName("");
        user.setFirstName("");
        user.setUpdatedAt(null);
        validateUser(user);
    }

    private <T> void validateUser(T object) {
        localValidatorFactory.afterPropertiesSet();
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactory.validate(object);
        System.out.println(constraintViolations);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void userModelConstraintsTest_Negative() {
        User user = new User();
        user.setId(1L);
        user.setEmail("InvalidEmail");
        user.setUpdatedAt(LocalDateTime.now());
        validateUserNegative(user, 1);
    }

    private <T> void validateUserNegative(T object, Integer violationNumber) {
        localValidatorFactory.afterPropertiesSet();
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactory.validate(object);
        System.out.println(constraintViolations);
        assertEquals(violationNumber, constraintViolations.size());
    }
}