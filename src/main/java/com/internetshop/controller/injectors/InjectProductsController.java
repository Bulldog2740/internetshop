package com.internetshop.controller.injectors;

import com.internetshop.exceptions.DataProcessingException;
import com.internetshop.lib.Injector;
import com.internetshop.model.Product;
import com.internetshop.service.ProductService;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/index/inject-products")
public class InjectProductsController extends HttpServlet {
    private static final Injector INJECTOR = Injector.getInstance("com.internetshop");
    private final ProductService productService
            = (ProductService) INJECTOR.getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        try {
            Product product1 = new Product("TINTIN B", new BigDecimal(15000));
            Product product2 = new Product("Starlink", new BigDecimal(30000));
            Product product3 = new Product("Starlink-1", new BigDecimal(30000));
            Product product4 = new Product("Starlink-2", new BigDecimal(30000));
            Product product5 = new Product("Starlink-3", new BigDecimal(30000));

            productService.create(product1);
            productService.create(product2);
            productService.create(product3);
            productService.create(product4);
            productService.create(product5);

            resp.sendRedirect(req.getContextPath() + "/");
        } catch (DataProcessingException e) {
            req.setAttribute("message", "You can't add product with similar name");
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
        }
    }
}
