package com.openclassrooms.starterjwt.user;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testUserServiceDelete() {
        Long id = 1L;
        doNothing().when(userRepository).deleteById(id);
        userService.delete(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUserServiceFindById_exist() {
        Long id = 1L;
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User foundUser = userService.findById(id);
        assertThat(foundUser).isEqualTo(user);
        verify(userRepository).findById(id);
    }

    @Test
    public void testUserServiceFindById_notExist() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        User foundUser = userService.findById(id);
        assertNull(foundUser);
        verify(userRepository).findById(id);
    }
}