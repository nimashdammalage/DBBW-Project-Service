package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.TripStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"season", "pencilBookings"})
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id", referencedColumnName = "id", nullable = false)
    private Season season;
    @OneToMany(mappedBy = "trip", fetch = FetchType.EAGER)
    List<PencilBooking> pencilBookings;
    @Column(length = 5)
    private String code;
    private int perPersonCost;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date ppColDate;
    private int passengerCount;
    @Enumerated(EnumType.STRING)
    private TripStatus tripStatus;
    private int createdBy;
    private Integer modifiedBy;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTimestamp;
}
