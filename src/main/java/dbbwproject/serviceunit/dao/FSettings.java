package dbbwproject.serviceunit.dao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FSettings {
    public static final String key = "settings";
    private int defaultPerPersonCost;
    private boolean defaultPerPersonCostEnable;
    private int defaultPPColDateGap;
    private boolean defaultPPColDateGapEnable;
    private int maxPBkCustomersPerDay;
    private boolean maxPBkCustomersPerDayEnable;
}
