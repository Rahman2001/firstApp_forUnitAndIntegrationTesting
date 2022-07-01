package repositories;

import domain.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StudentRepo extends CrudRepository<Student, Long> {
    @Query(value = "SELECT student FROM Student student WHERE student.name = ?1 and  student.email = ?2")
    Student findStudentByNameAndEmail(String name, String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Student student SET student.name = ?2, student.email = ?3  WHERE student.id = ?1 ")
    void updateById(long id, String updatedName, String updatedEmail);
}
