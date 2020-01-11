package dbbwproject.serviceunit.filter;

import dbbwproject.serviceunit.dao.Booking;
import dbbwproject.serviceunit.dto.BookingDto;
import dbbwproject.serviceunit.dto.BookingStatus;
import dbbwproject.serviceunit.dto.datatable.Column;
import dbbwproject.serviceunit.dto.datatable.Dir;
import dbbwproject.serviceunit.dto.datatable.Order;
import dbbwproject.serviceunit.mapper.BookingMapperImpl;
import dbbwproject.serviceunit.mapper.DateMapper;
import org.apache.commons.collections4.ComparatorUtils;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class BookingFilter extends AbstractFiler<Booking, BookingDto> {
    private final BookingMapperImpl bm;
    private QueryParam<String> seasonCode = new QueryParam<>("seasonCode", true, "b.registrationNumber.trip.season.code");
    private QueryParam<String> tripCode = new QueryParam<>("seasonCode", true, "b.registrationNumber.trip.code");
    private QueryParam<String> pbPersonName = new QueryParam<>("pbPersonName", true, "b.registrationNumber.pencilBooking.personName");
    private QueryParam<Date> tripStartDate = new QueryParam<>("tripStartDate", false, "b.registrationNumber.trip.startDate");
    private QueryParam<Integer> registrationNumber = new QueryParam<>("registrationNumber", false, "b.registrationNumber.regNumber");
    private QueryParam<BookingStatus> bookingStatus = new QueryParam<>("bookingStatus", false, "b.bookingStatus");
    private QueryParam<String> surName = new QueryParam<>("surName", true, "b.surName");
    private QueryParam<String> otherNames = new QueryParam<>("otherNames", true, "b.otherNames");
    private QueryParam<String> nicNo = new QueryParam<>("nicNo", true, "b.nicNo");

    public BookingFilter(EntityManagerFactory entityManagerFactory, BookingMapperImpl bm) {
        super(entityManagerFactory);
        tClass = Booking.class;
        this.bm = bm;
    }

    @Override
    protected void populateParams() {
        totalQuery = "Select count(*) from Booking";
        populateParam(seasonCode, (s) -> s);
        populateParam(tripCode, (s) -> s);
        populateParam(pbPersonName, (s) -> s);
        populateParam(tripStartDate, DateMapper::toDateObj);
        populateParam(registrationNumber, Integer::parseInt);
        populateParam(bookingStatus, BookingStatus::valueOf);
        populateParam(surName, (s) -> s);
        populateParam(otherNames, (s) -> s);
        populateParam(nicNo, (s) -> s);
        queryParams.add(seasonCode);
        queryParams.add(tripCode);
        queryParams.add(pbPersonName);
        queryParams.add(tripStartDate);
        queryParams.add(registrationNumber);
        queryParams.add(bookingStatus);
        queryParams.add(surName);
        queryParams.add(otherNames);
        queryParams.add(nicNo);
    }

    @Override
    protected void buildFilterQuery(List<QueryParam> searchableParamList) {
        filterQuery = !searchableParamList.isEmpty() ? "From Booking b where " : "From Booking b ";
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
        List<Comparator<Booking>> coms = new ArrayList<>();
        for (Order order : orders) {
            int colIndex = order.getColumn();
            String data = columns.get(colIndex).getData();
            Dir dir = order.getDir();
            createComparatorAndAdd(coms, data, dir, seasonCode, Comparator.comparing(b -> b.getRegistrationNumber().getTrip().getSeason().getCode()));
            createComparatorAndAdd(coms, data, dir, tripCode, Comparator.comparing(b -> b.getRegistrationNumber().getTrip().getCode()));
            createComparatorAndAdd(coms, data, dir, pbPersonName, Comparator.comparing(b -> b.getRegistrationNumber().getPencilBooking().getPersonName()));
            createComparatorAndAdd(coms, data, dir, tripStartDate, Comparator.comparing(b -> b.getRegistrationNumber().getTrip().getStartDate()));
            createComparatorAndAdd(coms, data, dir, registrationNumber, Comparator.comparing(b -> b.getRegistrationNumber().getRegNumber()));
            createComparatorAndAdd(coms, data, dir, bookingStatus, Comparator.comparing(Booking::getBookingStatus));
            createComparatorAndAdd(coms, data, dir, surName, Comparator.comparing(Booking::getSurName));
            createComparatorAndAdd(coms, data, dir, otherNames, Comparator.comparing(Booking::getOtherNames));
            createComparatorAndAdd(coms, data, dir, nicNo, Comparator.comparing(Booking::getNicNo));
        }
        if (!coms.isEmpty()) {
            resultList.sort(ComparatorUtils.chainedComparator(coms));
        }
    }

    @Override
    protected List<BookingDto> mapResultList(List<Booking> resultList) {
        return bm.mapBkToBkDtoList(resultList);
    }


}
