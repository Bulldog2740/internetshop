package com.internetshop.service;

import com.internetshop.model.Order;
import com.internetshop.model.ShoppingCart;
import com.internetshop.model.User;
import java.util.List;

public interface OrderService extends GenericService<Order, Long> {

    Order completeOrder(ShoppingCart shoppingCart);

    List<Order> getUserOrders(User user);

    boolean deleteProduct(Long id);
}
