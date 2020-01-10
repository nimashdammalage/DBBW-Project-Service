package dbbwproject.serviceunit.filter;

import dbbwproject.serviceunit.dao.Season;
import dbbwproject.serviceunit.dto.SeasonDto;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.datatable.Column;
import dbbwproject.serviceunit.dto.datatable.Dir;
import dbbwproject.serviceunit.dto.datatable.Order;
import dbbwproject.serviceunit.mapper.SeasonMapperImpl;
import org.apache.commons.collections4.ComparatorUtils;

import javax.persistence.EntityManagerFactory;
import java.util.*;

public class SeasonFilter extends AbstractFiler<Season, SeasonDto> {
    private final SeasonMapperImpl sm;
    private QueryParam<String> code = new QueryParam<>("code", true, "code");
    private QueryParam<SeasonStatus> status = new QueryParam<>("status", false, "status");

    public SeasonFilter(EntityManagerFactory entityManagerFactory, SeasonMapperImpl sm) {
        super(entityManagerFactory);
        tClass = Season.class;
        this.sm = sm;
    }

    @Override
    protected void populateParams(List<Column> colList) {
        totalQuery = "Select count(*) From Season";
        populateParam(code, colList, (col) -> code.getCode().equals(col.getData()), (s) -> s);
        populateParam(status, colList, (col) -> status.getCode().equals(col.getData()), SeasonStatus::valueOf);
        queryParams.add(code);
        queryParams.add(status);
    }

    @Override
    protected void buildFilterQuery(List<QueryParam> searchableParamList) {
        filterQuery = !searchableParamList.isEmpty() ? "From Season where " : "From Season ";
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
        List<Comparator<Season>> coms = new ArrayList<>();
        for (Order order : orders) {
            int colIndex = order.getColumn();
            String data = columns.get(colIndex).getData();
            Dir dir = order.getDir();
            if (code.isOrderly() && data.equals(code.getCode())) {
                addCmpByOrder(coms, dir, Comparator.comparing(Season::getCode));
            }
            if (status.isOrderly() && data.equals(status.getCode())) {
                addCmpByOrder(coms, dir, Comparator.comparing(Season::getStatus));
            }
        }
        if (!coms.isEmpty()) {
            resultList.sort(ComparatorUtils.chainedComparator(coms));
        }
    }

    @Override
    protected List<SeasonDto> mapResultList(List<Season> resultList) {
        return sm.mapSToSdtoList(resultList);
    }

}
