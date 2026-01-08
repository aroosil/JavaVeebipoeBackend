package ee.andu.server.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private boolean active;
    private int stock;

    @ManyToOne // teine toode võib sama kategooriat kasutada
    // @JoinColumn(name = "category_id")
    private Category category;

    @Nullable
    private String image;

    // olemas ka @OneToOne ---> ükski teine toode ei või seda kategooriat võtta
    //    @OneToOne(cascade = CascadeType.ALL)
    //    private Image image;
    //    @OneToOne
    //    private Ingredients ingredients;
}
