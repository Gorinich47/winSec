package my.web.app.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // id заказа

    @ManyToOne
    @JoinColumn(name="clients_id", nullable = false)
    private Client client; // клиент

    @ManyToOne

    @JoinColumn(name="address_id", nullable = false)
    private Addres address; // адрес замера

    private LocalDateTime date; // дата заказа
    private Long countWindows; // кол-во окон
    private String commets; // комментарии

}
