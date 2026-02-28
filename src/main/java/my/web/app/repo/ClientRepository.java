package my.web.app.repo;

import my.web.app.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     *  Поиск по полному имени
     * @param fullName
     * @return
     */
    Client findByFullName(String fullName);

    /**
     * Поиск по имени и фамилии
     * @param firstName
     * @param lastName
     * @return
     */
    Client findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Выбор всех и сортировка по имени и фамилии
     * @return
     */
    List<Client> findAllByOrderByFirstNameAscLastNameAsc();

    /**
     * Поиск по полному имени и номеру телефона
     * @param fullName
     * @param phoneNumber
     * @return
     */
    Client findByFullNameAndPhoneNumber(String fullName, String phoneNumber);
}
