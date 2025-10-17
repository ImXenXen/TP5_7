package com.tp7.cart.web;

import com.tp7.cart.ejb.CartBeanLocal;
import com.tp7.cart.model.Product;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    // Récupère le stateful EJB stocké en session, sinon le crée
    private CartBeanLocal getCartBean(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession(true);
        CartBeanLocal bean = (CartBeanLocal) session.getAttribute("CART_BEAN");
        if (bean == null) {
            InitialContext ic = new InitialContext();
            // JNDI portable Java EE 8 sur WildFly :
            bean = (CartBeanLocal) ic.lookup("java:module/CartBean!com.tp7.cart.ejb.CartBeanLocal");
            session.setAttribute("CART_BEAN", bean);
        }
        return bean;
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { route(req, resp); }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { req.setCharacterEncoding("UTF-8"); route(req, resp); }

    private void route(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = param(req, "action", "view");
        try {
            CartBeanLocal cartBean = getCartBean(req);

            switch (action) {
                case "add": {
                    String code = req.getParameter("code");
                    String label = req.getParameter("label");
                    String description = req.getParameter("description");
                    cartBean.addProductToCart(new Product(code, label, description));
                    req.setAttribute("message", "Produit ajouté au panier.");
                    forwardToCart(req, resp, cartBean);
                    break;
                }
                case "remove": {
                    String code = req.getParameter("code");
                    cartBean.removeFromCart(code);
                    req.setAttribute("message", "Produit retiré.");
                    forwardToCart(req, resp, cartBean);
                    break;
                }
                case "checkout": {
                    cartBean.checkOut();
                    req.setAttribute("items", cartBean.getLastCheckout());
                    // Si tu utilises @Remove sur checkOut(), fais :
                    // req.getSession().removeAttribute("CART_BEAN");
                    req.getRequestDispatcher("/summary.jsp").forward(req, resp);
                    break;
                }
                default:
                    forwardToCart(req, resp, cartBean);
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // log serveur
            req.getSession().removeAttribute("CART_BEAN");
            req.setAttribute("error", "Erreur: " + ex.getClass().getSimpleName() + " - " + (ex.getMessage() == null ? "" : ex.getMessage()));
            try {
                CartBeanLocal fresh = getCartBean(req);
                forwardToCart(req, resp, fresh);
            } catch (Exception e2) {
                throw new ServletException(e2);
            }
        }

        }
    

    private void forwardToCart(HttpServletRequest req, HttpServletResponse resp, CartBeanLocal cartBean)
            throws ServletException, IOException {
        req.setAttribute("items", cartBean.getCart());
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }

    private static String param(HttpServletRequest req, String k, String def) {
        String v = req.getParameter(k);
        return (v == null || v.isBlank()) ? def : v;
    }
}
