package com.internetshop.controller.product;

import com.internetshop.exceptions.DataProcessingException;
import com.internetshop.lib.Injector;
import com.internetshop.model.Product;
import com.internetshop.service.ProductService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/products/add")
public class AddProductController extends HttpServlet {
    private static final Injector INJECTOR = Injector.getInstance("com.internetshop");
    private final ProductService productService =
            (ProductService) INJECTOR.getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Product> allProducts = productService.getAll();
        req.setAttribute("products", allProducts);
        req.getRequestDispatcher("/WEB-INF/views/products/add.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        try {
            String name = req.getParameter("name");
            BigDecimal price = BigDecimal.valueOf(Long.parseLong(req.getParameter("price")));
            productService.create(new Product(name, price));
            resp.sendRedirect(req.getContextPath() + "/products/add");
        } catch (DataProcessingException e) {
            req.setAttribute("message", "You can't add product with similar name");
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
        }
    }
}
