package repositoryTest;

import com.example.firstapp_forjunitandintegrationtesting.FirstAppForUnitAndIntegrationTestsApplication;
import domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import repositories.StudentRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {FirstAppForUnitAndIntegrationTestsApplication.class})
@EnableAutoConfiguration
public class studentRepositoryTest {

    @Autowired
    private StudentRepo studentRepo;

    private Student student;

    @BeforeEach
    public void createStudent() {
        this.student = Student.builder().id(1L).name("rahman")
                .email("rahmanrejepov777@gmail.com").build();

        assertNotNull(this.studentRepo);
    }

    @Test
    public void studentRepoTest() {
        this.student = this.studentRepo.save(this.student);
        assertEquals(1L, this.student.getId());

        this.studentRepo.updateById(1, "Rahym", "rahmanrejepov777@gmail.com");
        assertEquals("Rahym", this.studentRepo.findById(1L).get().getName());
    }
}
