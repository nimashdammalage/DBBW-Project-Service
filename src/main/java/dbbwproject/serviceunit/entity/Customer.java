package dbbwproject.serviceunit.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(length = 500,nullable = false)
    private String name;

    @Column(unique = true,nullable = false)
    private String nicNumber;

    @Column
    private String email;
}
