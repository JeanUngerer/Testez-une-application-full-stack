package com.openclassrooms.starterjwt.user;

import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.utils.AuthUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    // Added UserRepository for database operations.

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthUtils authUtils;

    Long currentId = 0L;

    @BeforeEach
    public void setup(){
        String email = "testUser@example.com";
        String password = "password123";
        currentId = authUtils.newUser(email, password, "johnyy", "UserControlerTest");
    }

    @AfterEach
    public void teardown(){
        userRepository.deleteById(currentId);
    }



    // Writing mockMvc tests for "/api/user/{id}" endpoint GET method.
    @Test
    public void whenFindById_thenReturnUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", currentId)
                ).andExpect(status().isOk());
    }

    @Test
    public void whenFindById_thenReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", 100)
                ).andExpect(status().isNotFound());
    }

    @Test
    public void whenFindById_thenBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", "bad_request")
                ).andExpect(status().isBadRequest());
    }

    // Writing mockMvc tests for "/api/user/{id}" endpoint DELETE method.


    @Test
    public void whenDelete_thenReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", 100)
                ).andExpect(status().isNotFound());
    }



    @Test
    public void whenDelete_thenReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", "bad_request")
                ).andExpect(status().isBadRequest());
    }





}