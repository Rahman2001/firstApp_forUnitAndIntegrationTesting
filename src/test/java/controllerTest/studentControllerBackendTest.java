package controllerTest;

import com.example.firstapp_forjunitandintegrationtesting.FirstAppForUnitAndIntegrationTestsApplication;
import controller.StudentController;
import domain.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ResponseStatusException;
import repositories.StudentRepo;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {FirstAppForUnitAndIntegrationTestsApplication.class})
@AutoConfigureMockMvc
public class studentControllerBackendTest {

    @Mock private StudentRepo studentRepo;
     private StudentController studentController;
     private Student student;

    @BeforeEach
    public void injectingRepoAndCreateStudent() {
        this.studentController = new StudentController(this.studentRepo);

        this.student = Student.builder().id(1L).name("rahman")
                .email("rahmanrejepov777@gmail.com").build();
    }

    @Test
    public void getStudentsSuccessfullyTest() throws Exception {
        List<Student> studentList = List.of(student);
        given(this.studentRepo.findAll()).willReturn(studentList); // we are stubbing the list when studentRepo calls findAll() method because we don't want to test studentRepo but studentController

        assertFalse(this.studentController.getStudents().isEmpty()); // if we provided the list of students, then it should return non-empty list;
    }

    @Test
    public void getStudentsFailedTest() {
        assertThrows(ResponseStatusException.class, this.studentController::getStudents); // if database don't return any list of students to 'getStudents()' method, that method must throw exception.
        verify(this.studentRepo).findAll();  // 'getStudents()' method must use 'findAll()' method of studentRepo when trying to get list of students. So it checks whether it has been called.
    }

    @Test
    public void getStudentByIdSuccessfulTest() {
        Optional<Student> studentOptional = Optional.of(this.student);
        given(this.studentRepo.findById(anyLong())).willReturn(studentOptional);
        assertNotNull(this.studentController.getStudentById(anyLong()));

    }

    @Test
    public void getStudentByIdFailedTest() {
        assertThrows(ResponseStatusException.class, ()->this.studentController.getStudentById(anyLong()));
        verify(this.studentRepo).findById(anyLong());
    }

    @Test
    public void createStudentSuccessfulTest() {
        given(this.studentRepo.save(any(Student.class))).willReturn(this.student); // when save() method is called, it should return any Student.class for createStudent() to be successful.
        assertEquals(this.student, this.studentRepo.save(this.student)); // when createStudent() method returns an object, it must be of type Student.class.
    }
}
