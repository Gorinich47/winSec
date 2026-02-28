package my.web.app.repo;

import my.web.app.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Сортировка по полному имени клиента и дате заявки
     * @return
     */
    List<Order> findAllByOrderByClientFullNameAscDateAsc();

    List<Order> findAllByOrderByDateAsc();
}
