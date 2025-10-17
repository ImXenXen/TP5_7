package com.tp7.cart.ejb;

import com.tp7.cart.model.Product;
import javax.ejb.Local;
import java.util.List;

@Local
public interface CartBeanLocal {
    void addProductToCart(Product product);
    void checkOut();

    // utiles pour la servlet/JSP
    void removeFromCart(String code);
    List<Product> getCart();
    List<Product> getLastCheckout();
}
