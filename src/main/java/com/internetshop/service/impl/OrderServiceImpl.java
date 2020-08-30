package com.internetshop.service.impl;

import com.internetshop.dao.OrderDao;
import com.internetshop.lib.Inject;
import com.internetshop.lib.Service;
import com.internetshop.model.Order;
import com.internetshop.model.ShoppingCart;
import com.internetshop.model.User;
import com.internetshop.service.OrderService;
import com.internetshop.service.ShoppingCartService;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = create(new Order(new ArrayList<>(shoppingCart.getProducts()),
                shoppingCart.getUserId()));
        shoppingCartService.clear(shoppingCart);
        return order;
    }

    @Override
    public List<Order> getUserOrders(User user) {
        return orderDao.getUserOrders(user.getId());
    }

    @Override
    public Order create(Order element) {
        return orderDao.create(element);
    }

    @Override
    public Order get(Long id) {
        return orderDao.get(id).get();
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public Order update(Order element) {
        return orderDao.update(element);
    }

    @Override
    public boolean delete(Long id) {
        return orderDao.delete(id);
    }

    @Override
    public boolean deleteProduct(Long id) {
        return orderDao.deleteProductFromOrder(id);
    }
}
