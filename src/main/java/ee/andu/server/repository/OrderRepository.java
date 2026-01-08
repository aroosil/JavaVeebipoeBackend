package ee.andu.server.repository;

import ee.andu.server.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByCreatedBetween(Date startDate, Date endDate);
}
