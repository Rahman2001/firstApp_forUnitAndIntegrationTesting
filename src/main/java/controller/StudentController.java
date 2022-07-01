package controller;

import domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import repositories.StudentRepo;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentRepo studentRepo;

    @Autowired
    public StudentController(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @GetMapping("/getAllStudents")
    public List<Student> getStudents() {
        List<Student> allStudents = (List<Student>) this.studentRepo.findAll();
        if(!allStudents.isEmpty()) {
            return allStudents;
       } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are no any students in the system!");
        }
    }

    @GetMapping("/getStudent/{id}")
    public Student getStudentById(@PathVariable long id) {
        Optional<Student> student = this.studentRepo.findById(id);
        return student.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/updateStudent/{id}")
    public Student updateStudentById(@PathVariable long id, @Valid @RequestBody Student student) {
        Optional<Student> temp = this.studentRepo.findById(id);

        if (temp.isPresent()) {
            this.studentRepo.updateById(id, student.getName(), student.getEmail());
            return this.studentRepo.findById(id).get();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No student with such id exists in the system!");
    }

    @PostMapping("/createStudent")
    public Student createStudent(@Valid @RequestBody  Student student) {

        if(this.studentRepo.findStudentByNameAndEmail(student.getName(), student.getEmail()) == null) {
            return this.studentRepo.save(student);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
