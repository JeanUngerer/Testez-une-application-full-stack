package com.openclassrooms.starterjwt.teacher;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static com.openclassrooms.starterjwt.utils.ObjectAsJsonStrings.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void testFindById() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/" + teacher.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        Long invalidId = -1L;

        when(teacherService.findById(invalidId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/" + invalidId)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testFindAll() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        when(teacherService.findAll()).thenReturn(Collections.singletonList(teacher));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

}