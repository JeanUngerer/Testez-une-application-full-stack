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