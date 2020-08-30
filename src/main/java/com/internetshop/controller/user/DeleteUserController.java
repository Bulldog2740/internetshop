package com.internetshop.controller.user;

import com.internetshop.lib.Injector;
import com.internetshop.model.Order;
import com.internetshop.model.ShoppingCart;
import com.internetshop.service.OrderService;
import com.internetshop.service.ShoppingCartService;
import com.internetshop.service.UserService;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/users/delete")
public class DeleteUserController extends HttpServlet {
    private static final Injector INJECTOR = Injector.getInstance("com.internetshop");
    private final UserService userService = (UserService) INJECTOR.getInstance(UserService.class);
    private final ShoppingCartService shoppingCartService =
            (ShoppingCartService) INJECTOR.getInstance(ShoppingCartService.class);
    private final OrderService orderService =
            (OrderService) INJECTOR.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String userId = req.getParameter("id");
        Long id = Long.valueOf(userId);
        ShoppingCart shoppingCart = shoppingCartService.getByUserId(id);
        shoppingCartService.delete(shoppingCart.getId());
        List<Order> orders = orderService.getUserOrders(userService.get(id));
        for (Order order : orders) {
            orderService.delete(order.getId());
        }
        userService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/users/all");
    }
}
