package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.dao.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings,Integer> {
}
