package com.internetshop.dao.jdbc;

import com.internetshop.dao.OrderDao;
import com.internetshop.exceptions.DataProcessingException;
import com.internetshop.lib.Dao;
import com.internetshop.model.Order;
import com.internetshop.model.Product;
import com.internetshop.util.ConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;

@Dao
public class OrderDaoJdbcImpl implements OrderDao {
    private static final int ID_COLUMN = 1;
    private static final Logger LOGGER = Logger.getLogger(OrderDaoJdbcImpl.class);

    @Override
    public Order create(Order order) {
        String query = "INSERT INTO orders (user_id) VALUES (?);";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                PreparedStatement statement = connection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, order.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            Long orderId = resultSet.getLong(ID_COLUMN);
            order.setId(orderId);
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't INSERT order in mySQL internet_shop", ex);
        }
        insertOrdersProducts(order);
        LOGGER.info("Successful INSERT order in mySQL with ID " + order.getId());
        return order;
    }

    @Override
    public Optional<Order> get(Long id) {
        String query = "SELECT * FROM orders WHERE order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getCopyOfOrder(resultSet));
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't FIND order by ID "
                    + id + " in mySQL internet_shop", ex);
        }
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM orders;";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnectionInternetShop()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                orders.add(getCopyOfOrder(resultSet));
            }
            return orders;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't SELECT orders in mySQL internet_shop", ex);
        }
    }

    @Override
    public Order update(Order order) {
        String query = "UPDATE orders SET user_id = ? "
                + "WHERE order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                PreparedStatement statement = connection.prepareStatement(query);) {

            statement.setLong(1, order.getUserId());
            statement.setLong(2, order.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't UPDATE order by ID "
                    + order.getId()
                    + "in mySQL internet_shop", ex);
        }
        deleteOrderFromOrdersProducts(order.getId());
        insertOrdersProducts(order);
        LOGGER.info("Order with ID=" + order.getId() + " was UPDATED");
        return order;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM orders WHERE order_id = ?;";
        return deleteById(id, query);
    }

    public boolean deleteProductFromOrder(Long id) {
        String query = "DELETE FROM orders_products WHERE product_id = ?;";
        return deleteById(id, query);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        String query = "SELECT * FROM orders WHERE user_id = ?;";
        List<Order> userOrders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userOrders.add(getCopyOfOrder(resultSet));
            }
            return userOrders;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't FIND product by ID "
                    + userId + " in mySQL internet_shop", ex);
        }
    }

    private boolean deleteById(Long id, String query) {
        try (Connection connection = ConnectionUtil.getConnectionInternetShop()) {
            deleteOrderFromOrdersProducts(id);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            int numberOfRowsDeleted = statement.executeUpdate();
            return numberOfRowsDeleted != 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't DELETE order in mySQL with ID " + id, ex);
        }
    }

    private Order getCopyOfOrder(ResultSet resultSet) throws SQLException {
        Long orderId = resultSet.getLong("order_id");
        Long userId = resultSet.getLong("user_id");
        return new Order(orderId, getProductsOfOrder(orderId), userId);
    }

    private List<Product> getProductsOfOrder(Long orderId) {
        String query = "SELECT p.product_id, product_name, price  "
                + "FROM orders_products op INNER JOIN products p "
                + "ON op.product_id = p.product_id "
                + "WHERE op.order_id = ?;";
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnectionInternetShop()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long productId = resultSet.getLong("product_id");
                String name = resultSet.getString("product_name");
                BigDecimal price = resultSet.getBigDecimal("price");
                products.add(new Product(productId, name, price));
            }
            return products;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't FIND product from order by ID "
                    + orderId + " in mySQL internet_shop", ex);
        }
    }

    private void deleteOrderFromOrdersProducts(Long orderId) {
        String query = "DELETE FROM orders_products WHERE order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void insertOrdersProducts(Order order) {
        String insertOrdersProductsQuery = "INSERT INTO orders_products (order_id, product_id) "
                + "VALUES (?, ?);";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop()) {
            for (Product product : order.getProducts()) {
                PreparedStatement insertStatement =
                        connection.prepareStatement(insertOrdersProductsQuery);
                insertStatement.setLong(1, order.getId());
                insertStatement.setLong(2, product.getId());
                insertStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't INSERT products in order "
                    + "in mySQL internet_shop", ex);
        }
    }
}
