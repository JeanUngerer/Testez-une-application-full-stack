package com.openclassrooms.starterjwt.teacher;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/*
 * This class uses several annotations for controlling the execution of the tests. Let me briefly explain them.
 *
 * @SpringBootTeset: It indicates that the context loaded will be a SpringBootApplication. It is used to integrate
 * with all the features of Spring Boot including auto-configuration of mock and spying beans.
 *
 * @AutoConfigureMockMvc(addFilters = false): It ensures that a MockMvc instance is available. The 'addFilters = false'
 * attribute is to disable security or any other standard filters.
 *
 * @ActiveProfiles("test"): It activates 'test' profile for these tests. It is useful when you want to change
 * configuration for testing.
 *
 * @Autowired: It performs field injection of beans directly on the annotated field.
 *
 * @MockBean: It replaces any existing bean of the same type in the application context with a Mockito mock.
 * It's useful when you want to mock a bean and reset it after every test method execution.
 *
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    public void findAll_ShouldReturnListofTeachers() {
        Teacher teacher = new Teacher();
        List<Teacher> teachers = Collections.singletonList(teacher);

        when(teacherRepository.findAll()).thenReturn(teachers);
        List<Teacher> returnedTeachers = teacherService.findAll();

        verify(teacherRepository, times(1)).findAll();
        assertEquals(teachers, returnedTeachers);
    }

    @Test
    public void findById_ShouldReturnTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        Teacher returnedTeacher = teacherService.findById(1L);

        verify(teacherRepository, times(1)).findById(anyLong());
        assertEquals(teacher, returnedTeacher);
    }

    @Test
    public void findById_ShouldReturnNull() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());
        Teacher returnedTeacher = teacherService.findById(1L);

        verify(teacherRepository, times(1)).findById(anyLong());
        assertEquals(null, returnedTeacher);
    }
}