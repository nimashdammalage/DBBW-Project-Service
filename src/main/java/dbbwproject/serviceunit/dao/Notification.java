package dbbwproject.serviceunit.dao;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @Column(length = 200)
    private String code;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(length = 500)
    private String message;
    private Boolean read;

    public Notification(String code, Date createdDate, String message) {
        this.code = code;
        this.createdDate = createdDate;
        this.message = message;
        this.read = false;
    }

    public Notification() {
    }
}
