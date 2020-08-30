package com.internetshop.controller.order;

import com.internetshop.exceptions.DataProcessingException;
import com.internetshop.lib.Injector;
import com.internetshop.model.Order;
import com.internetshop.model.User;
import com.internetshop.service.OrderService;
import com.internetshop.service.UserService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/orders/byUser")
public class GetAllOrdersByUserController extends HttpServlet {
    private static final Injector INJECTOR = Injector.getInstance("com.internetshop");
    private final OrderService orderService =
            (OrderService) INJECTOR.getInstance(OrderService.class);
    private final UserService userService = (UserService) INJECTOR.getInstance(UserService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Long id = Long.valueOf(req.getParameter("id"));
            User user = userService.get(id);
            List<Order> allOrders = orderService.getUserOrders(user);
            req.setAttribute("orders", allOrders);
            req.setAttribute("id", user.getId());
            req.getRequestDispatcher("/WEB-INF/views/orders/byUser.jsp").forward(req, resp);
        } catch (DataProcessingException e) {
            req.setAttribute("message", "Incorrect user id");
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
        }
    }
}
