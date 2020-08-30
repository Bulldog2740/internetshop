package com.internetshop.controller.order;

import com.internetshop.lib.Injector;
import com.internetshop.model.ShoppingCart;
import com.internetshop.service.OrderService;
import com.internetshop.service.ShoppingCartService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/orders/create")
public class CompleteOrderController extends HttpServlet {
    private static final String USER_ID = "user_id";
    private static final Injector INJECTOR = Injector.getInstance("com.internetshop");
    private final ShoppingCartService shoppingCartService =
            (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);
    private final OrderService orderService =
            (OrderService) INJECTOR.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long userId = (Long) req.getSession().getAttribute(USER_ID);
        ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);
        if (shoppingCart.getProducts().size() > 0) {
            orderService.completeOrder(shoppingCart);
            req.setAttribute("message", "Your order successfully created");
            req.getRequestDispatcher("/WEB-INF/views/orders/userorderinfo.jsp").include(req, resp);
        } else {
            req.setAttribute("message", "Your shopping cart is empty");
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").include(req, resp);
        }

    }
}
