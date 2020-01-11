package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.PencilBookingStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"trip", "regNumbers"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pencil_booking")
public class PencilBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;
    @OneToMany(mappedBy = "pencilBooking", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RegNumber> regNumbers;
    @Column(length = 100)
    private String personName;
    @Column(length = 50)
    private String tpNo;
    private int seatCount;
    @Temporal(TemporalType.DATE)
    private Date meetUpDate;
    @Enumerated(EnumType.STRING)
    private PencilBookingStatus pencilBookingStatus;
    private int createdBy;
    private Integer modifiedBy;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTimestamp;

}
