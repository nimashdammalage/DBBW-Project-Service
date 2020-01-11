package dbbwproject.serviceunit.filter;

import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.dto.PencilBookingDto;
import dbbwproject.serviceunit.dto.PencilBookingStatus;
import dbbwproject.serviceunit.dto.datatable.Column;
import dbbwproject.serviceunit.dto.datatable.Dir;
import dbbwproject.serviceunit.dto.datatable.Order;
import dbbwproject.serviceunit.mapper.DateMapper;
import dbbwproject.serviceunit.mapper.PencilBookingMapperImpl;
import org.apache.commons.collections4.ComparatorUtils;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PencilBookingFilter extends AbstractFiler<PencilBooking, PencilBookingDto> {
    private final PencilBookingMapperImpl pm;
    private QueryParam<String> seasonCode = new QueryParam<>("seasonCode", true, "p.trip.season.code");
    private QueryParam<String> tripCode = new QueryParam<>("tripCode", true, "p.trip.code");
    private QueryParam<String> personName = new QueryParam<>("personName", true, "p.personName");
    private QueryParam<String> tpNo = new QueryParam<>("tpNo", false, "p.tpNo");
    private QueryParam<Integer> seatCount = new QueryParam<>("seatCount", false, "p.seatCount");
    //private QueryParam<List<Integer>> regNumbers = new QueryParam<>("regNumbers",true,"p.regNumbers.regNumber");
    // todo need to talk about this filter
    private QueryParam<Date> meetUpDate = new QueryParam<>("meetUpDate", false, "p.meetUpDate");
    private QueryParam<PencilBookingStatus> pencilBookingStatus = new QueryParam<>("pencilBookingStatus", false, "p.pencilBookingStatus");

    public PencilBookingFilter(EntityManagerFactory entityManagerFactory, PencilBookingMapperImpl pm) {
        super(entityManagerFactory);
        tClass = PencilBooking.class;
        this.pm = pm;
    }

    @Override
    protected void populateParams() {
        totalQuery = "Select count(*) from PencilBooking";
        populateParam(seasonCode, (s) -> s);
        populateParam(tripCode, (s) -> s);
        populateParam(personName, (s) -> s);
        populateParam(tpNo, (s) -> s);
        populateParam(seatCount, Integer::parseInt);
        populateParam(meetUpDate, DateMapper::toDateObj);
        populateParam(pencilBookingStatus, PencilBookingStatus::valueOf);
        queryParams.add(seasonCode);
        queryParams.add(tripCode);
        queryParams.add(personName);
        queryParams.add(tpNo);
        queryParams.add(seatCount);
        queryParams.add(meetUpDate);
        queryParams.add(pencilBookingStatus);
    }

    @Override
    protected void buildFilterQuery(List<QueryParam> searchableParamList) {
        filterQuery = !searchableParamList.isEmpty() ? "From PencilBooking p where " : "From PencilBooking p ";
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
        List<Comparator<PencilBooking>> coms = new ArrayList<>();
        for (Order order : orders) {
            int colIndex = order.getColumn();
            String data = columns.get(colIndex).getData();
            Dir dir = order.getDir();
            createComparatorAndAdd(coms, data, dir, seasonCode, Comparator.comparing(p -> p.getTrip().getSeason().getCode()));
            createComparatorAndAdd(coms, data, dir, tripCode, Comparator.comparing(p -> p.getTrip().getCode()));
            createComparatorAndAdd(coms, data, dir, personName, Comparator.comparing(PencilBooking::getPersonName));
            createComparatorAndAdd(coms, data, dir, tpNo, Comparator.comparing(PencilBooking::getTpNo));
            createComparatorAndAdd(coms, data, dir, seatCount, Comparator.comparing(PencilBooking::getSeatCount));
            createComparatorAndAdd(coms, data, dir, meetUpDate, Comparator.comparing(PencilBooking::getMeetUpDate));
            createComparatorAndAdd(coms, data, dir, pencilBookingStatus, Comparator.comparing(PencilBooking::getPencilBookingStatus));
        }
        if (!coms.isEmpty()) {
            resultList.sort(ComparatorUtils.chainedComparator(coms));
        }
    }

    @Override
    protected List<PencilBookingDto> mapResultList(List<PencilBooking> resultList) {
        return pm.mapPbToPbDtoList(resultList);
    }


}
