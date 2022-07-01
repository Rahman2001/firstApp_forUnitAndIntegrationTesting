package controllerTest;

import com.example.firstapp_forjunitandintegrationtesting.FirstAppForJUnitAndIntegrationTestingApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import repositories.StudentRepo;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {FirstAppForJUnitAndIntegrationTestingApplication.class})
@AutoConfigureMockMvc
public class restApiTest {
    @MockBean private StudentRepo studentRepo; // replaces a real bean in the application context with mock one because, in our case, we don't want to test real repo since we have already tested it in isolation.
    @Autowired private MockMvc mockMvc;
    private Student tempStudent;

    @BeforeEach
    public void createResource() {
        this.tempStudent = Student.builder().id(1L).name("rahman").email("rahmanrejepov777@gmail.com").build();
    }

    @Test
    public void getStudentsSuccessfulTest() throws Exception {
        when(this.studentRepo.findAll()).thenReturn(List.of(this.tempStudent)); // when 'findAll()' method of studentRepo is called in 'getStudents()' method of studentController (in our case), it returns that list

        this.mockMvc.perform(get("/api/student/getAllStudents")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

    }
    @Test
    public void getStudentsUnsuccessfulTest() throws Exception {
        this.mockMvc.perform(get("/api/student/getAllStudents")).andExpect(status().isBadRequest())
                .andExpect(status().reason("There are no any students in the system!"));
    }

    @Test
    public void updateStudentByIdSuccessfulTest() throws Exception {
        when(this.studentRepo.findById(1L)).thenReturn(Optional.of(this.tempStudent));
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/student/updateStudent/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.tempStudent)))
                .andExpect(jsonPath("$.id").value(1L)) // expected to have {...,"id":1L, ...} line in JSON response
                .andExpect(status().isOk());

        verify(this.studentRepo, times(2)).findById(anyLong()); // findById() method must be invoked only twice if it is going to be successful
        verify(this.studentRepo, times(1)).updateById(anyLong(), anyString(), anyString()); // updateById() method must be invoked only once in that method if it is going to be successful
    }

    @Test
    public void updateStudentByIdUnsuccessfulTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/student/updateStudent/{id}", 1L) // performing POST request with @PathVariable (1L)
                .contentType(MediaType.APPLICATION_JSON) // we send and receive raw JSON type data
                .content(objectMapper.writeValueAsString(this.tempStudent))) // when we want to send JSON data into the parameter of the method (b/c of @RequestBody), we use this object mapper in content()
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("No student with such id exists in the system!"));
    }
}
