package dbbwproject.serviceunit.filter;

import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dto.datatable.Column;
import dbbwproject.serviceunit.dto.datatable.Search;

import java.util.List;

public abstract class FFilter<T> {
    private static final Column col = new Column("data", "", true, true, new Search());

    public abstract List<T> filterList(List<T> input);

    protected <T extends Enum<T>> boolean enumMatch(T value, T input) {
        return value == null || value == input;
    }

    protected static <T extends Enum<T>> T extractEnum(Class<T> tClass, List<Column> columns, String code) {
        try {
            return Enum.valueOf(tClass, extractName(columns, code));
        } catch (Exception e) {
            return null;
        }
    }

    protected static String extractName(List<Column> columns, String code) {
        return columns.stream().filter(c -> code.equals(c.getData())).findFirst().orElse(col).getName();
    }

    protected static String extractNameWildCard(List<Column> columns, String code) {
        return extractName(columns, code) + "*";
    }
}
