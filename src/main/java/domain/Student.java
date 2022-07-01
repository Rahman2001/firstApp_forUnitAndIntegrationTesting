package domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    @NotNull(message = "Student name cannot be empty!")
    private String name;
    @Column
    @NotNull(message = "Student email cannot be empty!")
    private String email;

    public String toString() {
        return this.getId() + " " + this.getName() + " " + this.getEmail();
    }
}
