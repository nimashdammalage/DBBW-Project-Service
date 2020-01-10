package dbbwproject.serviceunit.dao;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer defaultPerPersonCost;
    @Column(nullable = false)
    private Boolean defaultPerPersonCostEnable;
    @Column(nullable = false)
    private Integer defaultPpColDateGap;
    @Column(nullable = false)
    private Boolean defaultPpColDateGapEnable;
    @Column(nullable = false)
    private Integer maxPbkCustomersPerDay;
    @Column(nullable = false)
    private Boolean maxPbkCustomersPerDayEnable;
}
