package ee.andu.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date created;
    private double total;

    @ManyToOne
    private Person person;

    @ManyToMany
    private List<Product> products; // tabeli nimi orders_products kuna manytomany tekitab seosed kahe tabeli vahele uues tabelis

    private String parcelMachine;
    private PaymentState paymentState;

}
