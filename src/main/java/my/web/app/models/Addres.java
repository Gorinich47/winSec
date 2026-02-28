package my.web.app.models;

import jakarta.persistence.*;
import lombok.*;

@Data
//@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Addres {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String house;
    private String flat;

    public Addres(String city, String street, String house) {
        if (city != null && street != null && house != null) {
            this.city = city;
            this.street = street;
            this.house = house;
        } else {
            throw new IllegalArgumentException("City, street and house must not be null");
        }
    }
    public Addres(String city, String street, String house, String flat) {
        if (city != null && street != null && house != null && flat != null) {
                this.city = city;
                this.street = street;
                this.house = house;
                this.flat = (flat.equals("null")) ? null : flat;
        } else {
            throw new IllegalArgumentException("City, street, house and flat must not be null");
        }
    }

    public String toString() {
        if (flat == null || flat.isEmpty()) {
            return String.format("%s, %s, дом %s", city , street ,house);
        } else {
            return String.format("%s, %s, дом %s, кв %s", city , street, house, flat);
        }
    }

}
