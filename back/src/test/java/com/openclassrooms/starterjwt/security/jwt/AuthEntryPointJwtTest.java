package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is a unit test for AuthEntryPointJwt class.
 * The class utilizes several testing and mocking utilities provided by the Spring, Mockito, and JUnit frameworks.
 *
 * It is annotated with @SpringBootTest to indicate that it is a context-based test and to load
 * an ApplicationContext.
 *
 * @ActiveProfiles is used to pick 'test' profile environment.&
 *
 * To disable installing of default spring security filters, @AutoConfigureMockMvc(addFilters = false)
 * is used.
 *
 * With @InjectMocks, Mockito injects mocks created by MockitoAnnotations.openMocks(this) into
 * authEntryPointJwt private field.
 * The @BeforeEach-annotated setup() method initializes resources needed for each test.
 * MockHttpServletRequest and MockHttpServletResponse are mock implementations of the HttpServletRequest
 * and HttpServletResponse interfaces, respectively.
 * CredentialsExpiredException represents an authentication exception for test scenarios.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class AuthEntryPointJwtTest {
    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private CredentialsExpiredException authException;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        authException = new CredentialsExpiredException("TEST");
    }

    @Test
    public void testCommence() throws Exception {
        authEntryPointJwt.commence(request, response, authException);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("application/json", response.getContentType());
        assertEquals("{\"path\":\"\",\"error\":\"Unauthorized\",\"message\":\"TEST\",\"status\":401}",
          response.getContentAsString());
    }
}