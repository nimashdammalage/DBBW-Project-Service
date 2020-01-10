package dbbwproject.serviceunit.filter;

import dbbwproject.serviceunit.dao.Season;
import dbbwproject.serviceunit.dto.datatable.Column;
import dbbwproject.serviceunit.dto.datatable.DtReqDto;
import dbbwproject.serviceunit.dto.datatable.DtResponse;
import dbbwproject.serviceunit.dto.datatable.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractFiler<T,X> {
    private SessionFactory sm;
    private Class<T> tClass;
    List<QueryParam> queryParams = new ArrayList<>();
    String filterQuery;
    private Integer totalCount;
    private Integer filterCount;
    List<T> resultList = new ArrayList<>();
    String totalQuery = "";

    @Autowired
    protected AbstractFiler(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sm = entityManagerFactory.unwrap(SessionFactory.class);
    }

    public DtResponse<X> filter(DtReqDto request) {
        request.validate();
        List<Column> colList = request.getColumns();
        List<Order> orderList = request.getOrder();
        int drawNo = request.getDraw();
        populateParams(colList);

        List<QueryParam> seachableParamList = queryParams.stream().filter(QueryParam::isSearchable).collect(Collectors.toList());
        buildFilterQuery(seachableParamList);
        populateTotalDBRecords(totalQuery);
        populateTotalFilterCount(seachableParamList);
        populateResultList(seachableParamList, request.getStart(), request.getLength());
        orderResult(orderList, colList);
        return wrapResponse(drawNo);
    }

    private DtResponse<X> wrapResponse(Integer drawNo) {
        DtResponse<X> res = new DtResponse<>();
        res.setDraw(drawNo);
        List<X> mappedResultList = mapResultList(resultList);
        res.setData(mappedResultList);
        res.setRecordsFiltered(filterCount);
        res.setRecordsTotal(totalCount);
        return res;
    }

    protected abstract List<X> mapResultList(List<T> resultList);

    public abstract void orderResult(List<Order> orders, List<Column> columns);

    private void populateTotalFilterCount(List<QueryParam> searchableParamList) {
        String countQuery = "select count(*) " + filterQuery;
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Integer> cq = session.createQuery(countQuery, Integer.class);

        buildQueryValues(searchableParamList, cq);
        filterCount = cq.getSingleResult();
        session.getTransaction().commit();
    }

    private void populateResultList(List<QueryParam> searchableParamList, int firstResult, int count) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<T> dbQuery = session.createQuery(this.filterQuery, tClass);
        buildQueryValues(searchableParamList, dbQuery);
        dbQuery.setFirstResult(firstResult);
        dbQuery.setMaxResults(count);
        resultList = dbQuery.getResultList();
        session.getTransaction().commit();
    }

    private <K> void buildQueryValues(List<QueryParam> searchableParamList, Query<K> dbQuery) {
        for (int i = 0; i < searchableParamList.size(); i++) {
            if (searchableParamList.get(i).isWildCard()) {
                fillWildCardValue(searchableParamList.get(i).getQueryValue(), dbQuery, i);
            } else {
                fillValue(searchableParamList.get(i).getQueryValue(), dbQuery, i);
            }
        }
    }

    private <V> void fillValue(V value, Query dbQuery, int i) {
        dbQuery.setParameter(i + 1, value);
    }

    private <V> void fillWildCardValue(V value, Query dbQuery, int i) {
        dbQuery.setParameter(i + 1, "%" + value + "%");
    }

    private void populateTotalDBRecords(String query) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Integer> totalq = session.createQuery(query, Integer.class);
        totalCount = totalq.getSingleResult();
        session.getTransaction().commit();
    }

    String fillClause(List<QueryParam> searchableParamList, int index, String comparator) {
        return filterQuery + searchableParamList.get(index).getQueryField() + comparator + "?" + (index + 1) + " ";
    }

    protected abstract void buildFilterQuery(List<QueryParam> searchableParamList);

    protected abstract void populateParams(List<Column> colList);


    <U> void populateParam(QueryParam<U> q, List<Column> colList, Predicate<Column> filterPredicate, Function<String, U> dataFunction) {
        Optional<Column> seletedCol = colList.stream().filter(filterPredicate).findFirst();
        if (seletedCol.isPresent() && seletedCol.get().isSearchable()) {
            q.setQueryValue(dataFunction.apply(seletedCol.get().getData()));
            q.setSearchable(true);
        }
    }


}
