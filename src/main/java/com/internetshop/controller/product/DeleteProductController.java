package com.internetshop.controller.product;

import com.internetshop.lib.Injector;
import com.internetshop.service.OrderService;
import com.internetshop.service.ProductService;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/products/delete")
public class DeleteProductController extends HttpServlet {
    private static final Injector INJECTOR = Injector.getInstance("com.internetshop");
    private final ProductService productService =
            (ProductService) INJECTOR.getInstance(ProductService.class);
    private final OrderService orderService =
            (OrderService) INJECTOR.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String id = req.getParameter("id");
        Long productId = Long.parseLong(id);
        orderService.deleteProduct(productId);
        productService.delete(productId);
        resp.sendRedirect(req.getContextPath() + "/products/add");
    }
}
