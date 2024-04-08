package com.openclassrooms.starterjwt.session;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.utils.AuthUtils;
import org.junit.jupiter.api.*;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static com.openclassrooms.starterjwt.utils.ObjectAsJsonStrings.asJsonString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class SessionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    SessionMapper sessionMapper;

    @MockBean
    SessionService sessionService;

    @SpyBean
    SessionService sessionServiceSpy;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testFindById() throws Exception {
        Session session = saveSession();
        when(sessionService.getById(session.getId())).thenReturn(session);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/" + session.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testFindAll() throws Exception {
        Session session = saveSession();
        when(sessionService.findAll()).thenReturn(Collections.singletonList(session));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testCreate() throws Exception {
        Session session = saveSession();
        SessionDto sessionDto = sessionMapper.toDto(session);
        String jsonRequest = asJsonString(sessionDto);

        when(sessionService.create(session)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testUpdate() throws Exception {
        Session session = saveSession();
        SessionDto sessionDto = sessionMapper.toDto(session);
        String jsonRequest = asJsonString(sessionDto);
        Long sessionId = session.getId();
        session.setName("updated");
        when(sessionService.update(sessionId, session)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/" + sessionId)
                .content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testParticipate() throws Exception {
        Session session = saveSession();
        Long sessionId = session.getId();
        Long userId = session.getUsers().get(0).getId();
        Mockito.doNothing().when(sessionServiceSpy).participate(sessionId, userId);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/" + sessionId + "/participate/" + userId)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testNoLongerParticipate() throws Exception {
        Session session = saveSession();
        Long sessionId = session.getId();
        Long userId = session.getUsers().get(0).getId();
        Mockito.doNothing().when(sessionServiceSpy).noLongerParticipate(sessionId, userId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/" + sessionId + "/participate/" + userId)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    private Session saveSession(){
        Session testSession;

        testSession = new Session();
        testSession.setName("Test Session");
        testSession.setDate(new Date());
        testSession.setDescription("Test Session Description");
        testSession.setId(1L);

        Teacher teacher = new Teacher();   // Assuming Teacher object's details are set preferably from a mock repository
        teacher.setFirstName("firstname");
        teacher.setLastName("lastname");
        teacher.setId(1L);


        User user = User.builder()
                .id(1L)
                .email("testUser@example.com")
                .password(passwordEncoder.encode("password123"))
                .firstName("john")
                .lastName("Doe")
                .build();

        testSession.setTeacher(teacher);

        List<User> users = Collections.singletonList(user); // Assuming User object's details are set preferably from a mock repository
        testSession.setUsers(users);
        return testSession;
    }
}