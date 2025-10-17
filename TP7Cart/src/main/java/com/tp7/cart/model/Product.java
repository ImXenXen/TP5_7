package com.tp7.cart.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String code;

  
    @Column(name = "libelle", nullable = false, length = 50)
    private String label;

    @Lob
    private String description;

    public Product() {}
    public Product(String code, String label, String description) {
        this.code = code;
        this.label = label;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product p = (Product) o;
        return Objects.equals(code, p.code);
    }
    @Override public int hashCode() { return Objects.hash(code); }
}
