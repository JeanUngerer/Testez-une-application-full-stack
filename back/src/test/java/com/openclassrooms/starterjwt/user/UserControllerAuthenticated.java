package com.openclassrooms.starterjwt.user;

import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.utils.AuthUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
public class UserControllerAuthenticated {

    @Autowired
    MockMvc mockMvc;

    // Added UserRepository for database operations.

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthUtils authUtils;



    @Test
    public void whenDelete_thenReturnOk() throws Exception {
        String email = "testUserControllerUnauthorized@example.com";
        String password = "password123";
        Long userId = authUtils.newUser(email, password, "johny2", "UserController");
        String userToken = authUtils.getToken(email, password);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId)
                .header("Authorization", "Bearer " + userToken)
        ).andExpect(status().isOk());
    }


    @Test
    public void whenDelete_thenReturnUnauthorized() throws Exception {
        // TODO: set up a different user in the security context.
        String email = "testUserUnauthorized@example.com";
        String password = "password123";
        Long userId = authUtils.newUser(email, password, "johny2", "UserUnauthorized");
        String userToken = authUtils.getToken(email, password);

        String newEmail = "testNewUserController@example.com";
        String newPassword = "password123";
        Long newUserId = authUtils.newUser(newEmail, newPassword, "johny2", "NewUserController");
        String newUserToken = authUtils.getToken(email, password);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", newUserId)
                .header("Authorization", "Bearer " + userToken)).andExpect(status().isUnauthorized());
        userRepository.deleteById(newUserId);
        userRepository.deleteById(userId);

    }
}
