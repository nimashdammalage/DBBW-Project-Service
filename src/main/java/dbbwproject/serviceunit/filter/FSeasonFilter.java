package dbbwproject.serviceunit.filter;

import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.datatable.Column;
import lombok.*;
import org.apache.commons.io.FilenameUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class FSeasonFilter extends FFilter<FSeason> {

    private String codeWildCard;
    private SeasonStatus status;
    private String createdByWildCard;
    private String modifiedByWildCard;

    private FSeasonFilter() {
    }

    public static FSeasonFilter of(List<Column> columns) {
        FSeasonFilter fs = new FSeasonFilter();
        fs.codeWildCard = extractNameWildCard(columns, FSeason.CODE);
        fs.status = extractEnum(SeasonStatus.class, columns, FSeason.STATUS);
        fs.createdByWildCard = extractNameWildCard(columns, FSeason.CREATED_BY);
        fs.modifiedByWildCard = extractNameWildCard(columns, FSeason.MODIFIED_NY);
        return fs;
    }

    @Override
    public List<FSeason> filterList(List<FSeason> input) {
        return input.stream()
                .filter(s -> FilenameUtils.wildcardMatch(s.getCode(), codeWildCard))
                .filter(s -> enumMatch(status, s.getStatus()))
                .filter(s -> FilenameUtils.wildcardMatch(s.getCreatedBy(), createdByWildCard))
                .filter(s -> FilenameUtils.wildcardMatch(s.getModifiedBy(), modifiedByWildCard))
                .collect(Collectors.toList());
    }
}
