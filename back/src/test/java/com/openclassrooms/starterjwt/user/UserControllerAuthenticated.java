package com.openclassrooms.starterjwt.user;

import com.openclassrooms.starterjwt.repository.UserRepository;
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
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerAuthenticated {

    @Autowired
    MockMvc mockMvc;

    // Added UserRepository for database operations.

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;





    @Test
    public void whenDelete_thenReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", currentId)
        ).andExpect(status().isOk());
    }


    @Test
    public void whenDelete_thenReturnUnauthorized() throws Exception {
        // TODO: set up a different user in the security context.
        String email = "testUserControllerUnauthorized@example.com";
        String password = "password123";
        Long newUserId = newUser(email, password, "johny2", "UserControllerUnauthorized");
        String newUserToken = getToken(email, password);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", currentId)
                .header("Authorization", "Bearer " + newUserToken)).andExpect(status().isUnauthorized());
        userRepository.deleteById(newUserId);

    }
}
