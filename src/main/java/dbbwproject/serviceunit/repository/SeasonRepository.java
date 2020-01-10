package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.dao.Season;
import dbbwproject.serviceunit.dto.SeasonStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Integer> {
    Optional<Season> findSeasonByCode(String code);

    Optional<List<Season>> getSeasonByStatus(SeasonStatus seasonStatus);
}
