package dbbwproject.serviceunit.dao;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(exclude = {"pencilBooking", "trip"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reg_number")
public class RegNumber {

    public RegNumber(PencilBooking pencilBooking, Trip trip, int regNumber) {
        this.pencilBooking = pencilBooking;
        this.trip = trip;
        this.regNumber = regNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pb_id", referencedColumnName = "id", nullable = false)
    private PencilBooking pencilBooking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;

    private Integer regNumber;

}
