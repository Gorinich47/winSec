package my.web.app.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    private String fullName;
    @Column(nullable = false)
    private String phoneNumber;

    /* хранить клиента с полным именем из фамилии и имя не очень хорошо, т.к. могут быть двойники
    * пока предположим адрес клиента поможет избежать путаницы
    */
    public Client(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.phoneNumber = phoneNumber;
    }
}
