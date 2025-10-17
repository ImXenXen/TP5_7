package com.tp7.cart.ejb;

import com.tp7.cart.model.Product;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional; // Java EE 8 dispose de javax.transaction.Transactional
import java.util.ArrayList;
import java.util.List;

@Stateful
public class CartBean implements CartBeanLocal {

    private final List<Product> cart = new ArrayList<>();
    private List<Product> lastCheckout = new ArrayList<>();

    @PersistenceContext(unitName = "tp7PU")
    private EntityManager em;

    @Override
    public void addProductToCart(Product product) {
        if (product == null || product.getCode() == null || product.getCode().isBlank())
            throw new IllegalArgumentException("Code requis");
        if (product.getLabel() == null || product.getLabel().isBlank())
            throw new IllegalArgumentException("Libellé requis");
        if (product.getLabel().length() > 50)
            throw new IllegalArgumentException("Libellé > 50 caractères");

        cart.removeIf(p -> p.getCode().equalsIgnoreCase(product.getCode()));
        cart.add(product);
    }

    @Override
    public void removeFromCart(String code) {
        if (code != null) cart.removeIf(p -> p.getCode().equalsIgnoreCase(code));
    }

    @Override
    public List<Product> getCart() { return new ArrayList<>(cart); }

    @Override
    public List<Product> getLastCheckout() { return lastCheckout; }

    @Override
    public void checkOut() {
        lastCheckout = new ArrayList<>(cart);
        for (Product p : cart) {
            Product existing = em.createQuery(
                    "SELECT x FROM Product x WHERE x.code = :c", Product.class)
                    .setParameter("c", p.getCode())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (existing == null) {
                em.persist(p);
            } else {
                existing.setLabel(p.getLabel());
                existing.setDescription(p.getDescription());
                em.merge(existing);
            }
        }
        cart.clear();
    }
}
