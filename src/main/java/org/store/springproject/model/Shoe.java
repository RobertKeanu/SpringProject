package org.store.springproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shoe")
public class Shoe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "shoe_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "shoe_name", nullable = false)
    private String shoeName;

    @Column(name = "size", nullable = false)
    private int size;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "price", nullable = false)
    private double price;

    public Shoe(String shoeName, int size, int stock, double price) {
        this.shoeName = shoeName;
        this.size = size;
        this.stock = stock;
        this.price = price;
    }

//    public Shoe(String shoeName, int size, int stock, double price, Long id) {
//        this.shoeName = shoeName;
//        this.size = size;
//        this.stock = stock;
//        this.price = price;
//        this.id = id;
//    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "shoe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

}