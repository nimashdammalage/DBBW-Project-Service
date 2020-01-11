package dbbwproject.serviceunit.filter;

import dbbwproject.serviceunit.dao.Trip;
import dbbwproject.serviceunit.dto.TripDto;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.dto.datatable.Column;
import dbbwproject.serviceunit.dto.datatable.Dir;
import dbbwproject.serviceunit.dto.datatable.Order;
import dbbwproject.serviceunit.mapper.DateMapper;
import dbbwproject.serviceunit.mapper.TripMapperImpl;
import org.apache.commons.collections4.ComparatorUtils;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TripFilter extends AbstractFiler<Trip, TripDto> {
    private final TripMapperImpl tm;
    private QueryParam<String> code = new QueryParam<>("code", true, "t.code");
    private QueryParam<String> seasonCode = new QueryParam<>("seasonCode", true, "t.season.code");
    private QueryParam<Integer> perPersonCost = new QueryParam<>("perPersonCost", false, "t.perPersonCost");
    private QueryParam<Date> startDate = new QueryParam<>("startDate", false, "t.startDate");
    private QueryParam<Date> endDate = new QueryParam<>("endDate", false, "t.endDate");
    private QueryParam<Date> ppColDate = new QueryParam<>("ppColDate", false, "t.ppColDate");
    private QueryParam<Integer> passengerCount = new QueryParam<>("passengerCount", false, "t.passengerCount");
    private QueryParam<TripStatus> tripStatus = new QueryParam<>("tripStatus", false, "t.tripStatus");

    public TripFilter(EntityManagerFactory entityManagerFactory, TripMapperImpl tm) {
        super(entityManagerFactory);
        tClass = Trip.class;
        this.tm = tm;
    }

    @Override
    protected void populateParams() {
        totalQuery = "Select count(*) from Trip";
        populateParam(code, (s) -> s);
        populateParam(seasonCode, (s) -> s);
        populateParam(perPersonCost, Integer::parseInt);
        populateParam(startDate, DateMapper::toDateObj);
        populateParam(endDate, DateMapper::toDateObj);
        populateParam(ppColDate, DateMapper::toDateObj);
        populateParam(passengerCount, Integer::parseInt);
        populateParam(tripStatus, TripStatus::valueOf);
        queryParams.add(code);
        queryParams.add(seasonCode);
        queryParams.add(perPersonCost);
        queryParams.add(startDate);
        queryParams.add(endDate);
        queryParams.add(ppColDate);
        queryParams.add(passengerCount);
        queryParams.add(tripStatus);
    }

    @Override
    protected void buildFilterQuery(List<QueryParam> searchableParamList) {
        filterQuery = !searchableParamList.isEmpty() ? "From Trip t where " : "From Trip t ";
        for (int i = 0; i < searchableParamList.size(); i++) {
            if (searchableParamList.get(i).isWildCard()) {
                filterQuery = fillClause(searchableParamList, i, " like ");
            } else {
                filterQuery = fillClause(searchableParamList, i, " = ");
            }
            if (i < searchableParamList.size() - 1) {
                filterQuery = filterQuery + "and ";
            }
        }
        filterQuery = filterQuery + "order by createdTimestamp desc";
    }

    @Override
    public void orderResult(List<Order> orders, List<Column> columns) {
        List<Comparator<Trip>> coms = new ArrayList<>();
        for (Order order : orders) {
            int colIndex = order.getColumn();
            String data = columns.get(colIndex).getData();
            Dir dir = order.getDir();
            createComparatorAndAdd(coms, data, dir, code, Comparator.comparing(Trip::getCode));
            createComparatorAndAdd(coms, data, dir, seasonCode, Comparator.comparing(t -> t.getSeason().getCode()));
            createComparatorAndAdd(coms, data, dir, perPersonCost, Comparator.comparing(Trip::getPerPersonCost));
            createComparatorAndAdd(coms, data, dir, startDate, Comparator.comparing(Trip::getStartDate));
            createComparatorAndAdd(coms, data, dir, endDate, Comparator.comparing(Trip::getEndDate));
            createComparatorAndAdd(coms, data, dir, ppColDate, Comparator.comparing(Trip::getPpColDate));
            createComparatorAndAdd(coms, data, dir, passengerCount, Comparator.comparing(Trip::getPassengerCount));
            createComparatorAndAdd(coms, data, dir, tripStatus, Comparator.comparing(Trip::getTripStatus));
        }
        if (!coms.isEmpty()) {
            resultList.sort(ComparatorUtils.chainedComparator(coms));
        }
    }

    @Override
    protected List<TripDto> mapResultList(List<Trip> resultList) {
        return tm.mapTToTdtoList(resultList);
    }
}
