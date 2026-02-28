package my.web.app.repo;

import my.web.app.models.Addres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddresRepository extends JpaRepository<Addres, Long> {

    Addres findByCityAndStreetAndHouseAndFlat(String city, String street, String house, String flat);
}
