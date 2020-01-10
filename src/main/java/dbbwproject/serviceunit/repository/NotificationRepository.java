package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.dao.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
