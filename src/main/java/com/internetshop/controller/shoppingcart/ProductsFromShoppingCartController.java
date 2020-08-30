package com.internetshop.controller.shoppingcart;

import com.internetshop.lib.Injector;
import com.internetshop.model.ShoppingCart;
import com.internetshop.service.ShoppingCartService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/shoppingcart")
public class ProductsFromShoppingCartController extends HttpServlet {
    private static final String USER_ID = "user_id";
    private static final Injector INJECTOR = Injector.getInstance("com.internetshop");
    private final ShoppingCartService shoppingCartService =
            (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute(USER_ID);
        ShoppingCart byUserId = shoppingCartService.getByUserId(userId);
        req.setAttribute("products", byUserId.getProducts());
        req.getRequestDispatcher("/WEB-INF/views/shoppingcart/all.jsp").forward(req, resp);
    }
}
