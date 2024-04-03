package com.openclassrooms.starterjwt.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.openclassrooms.starterjwt.utils.ObjectAsJsonStrings.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    Long currentId = 0L;

    @BeforeEach
    public void setup(){
        String email = "testUser@example.com";
        String password = "password123";
        currentId = newUser(email, password, "johnyy", "UserControlerTest");
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




    public String getToken(String username, String password) throws Exception {
        String url = "/api/auth/login";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(username);
        loginRequest.setPassword(password);

        userRepository.save(User.builder()
                .email("testUser@example.com")
                .password(passwordEncoder.encode("password123"))
                .firstName("John")
                .lastName("Doe")
                .build());

        MvcResult result = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        return jsonNode.get("token").asText();
    }

    public Long newUser(String email, String password, String firstname, String lastname){
        return userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstname)
                .lastName(lastname)
                .build()).getId();
    }
}