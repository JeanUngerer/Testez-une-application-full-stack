package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class JwtUtilsTests {

    @MockBean
    private UserDetailsImpl userDetails;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void generateJwtTokenTest() {
        String username = "testUsername";
        String password = "password";
        when(userDetails.getUsername()).thenReturn(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, new HashSet<>());
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        assertThat(jwtToken).isNotEmpty();
        assertThat(jwtUtils.getUserNameFromJwtToken(jwtToken)).isEqualTo(username);
    }

    @Test
    public void validateTokenTest() {
        String username = "username";
        String password = "password";
        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, new HashSet<>());
        String jwt = jwtUtils.generateJwtToken(authentication);
        assertThat(jwtUtils.validateJwtToken(jwt)).isTrue();
        assertThat(jwtUtils.validateJwtToken(jwt + "error")).isFalse();
        assertThat(jwtUtils.validateJwtToken("")).isFalse();
        assertThat(jwtUtils.validateJwtToken(null)).isFalse();
    }

    @Test
    public void getUserNameFromJwtTokenTest() {
        String username = "testUsername";
        String password = "password";
        when(userDetails.getUsername()).thenReturn(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, new HashSet<>());
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        assertThat(jwtUtils.getUserNameFromJwtToken(jwtToken)).isEqualTo(username);
    }

    @Test
    public void extractUsernameTokenFailureTest() {
        String invalidToken = "invalidToken";
        assertThrows(MalformedJwtException.class, () -> jwtUtils.getUserNameFromJwtToken(invalidToken));
    }

    @Test
    public void validateTokenInvalidSignatureTest() {
        String username = "username";
        String password = "password";
        when(userDetails.getUsername()).thenReturn(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, new HashSet<>());

        String jwt = jwtUtils.generateJwtToken(authentication);
        assertThat(jwtUtils.validateJwtToken(jwt + "invalid_signature")).isFalse();
    }
}