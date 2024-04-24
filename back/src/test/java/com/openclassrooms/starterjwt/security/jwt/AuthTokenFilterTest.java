package com.openclassrooms.starterjwt.security.jwt;

/**
 * The following class showcases the testing and mocking annotations within a test case.
 *
 * These annotations are an integral part of Mockito framework which is used for effective unit testing in Java.
 * The annotations make our test code clean and manageable.
 *
 * Mockito maintains a very nice balance between the flexibility and expressiveness of the tests.
 *
 * @ExtendWith : is a means to have custom post-processing code to further configure the instantiated test class.
 *
 * @InjectMocks : is used to inject mock fields into the tested object automatically.
 *
 * @Mock : shorthand for creating mock instance.
 *
 * Mocking allows us to isolate the class being tested and hence if the test fails,
 * we are certain about the class at fault and need not doubt other collaborators/ real objects.
 *
 * Mocks also allow us to verify the calls the system under test makes on its collaborators.
 *
 */
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;
    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserDetails userDetails;
    @Mock
    private JwtUtils jwtUtils;

    @Test
    void doFilterInternalValidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtils.validateJwtToken("token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("token")).thenReturn("mail@test");
        when(userDetailsService.loadUserByUsername("mail@test")).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(userDetailsService).loadUserByUsername("mail@test");
        verify(userDetails).getAuthorities();
    }

    @Test
    void doFilterInternalNotValidToken() throws ServletException, IOException {
        UserDetails userDetails = new UserDetailsImpl(1L,"mail@test","Michel","Blanc",false,"pwdTest");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid");

        authTokenFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        verify(filterChain).doFilter(request, response);

        assertNotEquals(SecurityContextHolder.getContext().getAuthentication(), authentication);
    }
}