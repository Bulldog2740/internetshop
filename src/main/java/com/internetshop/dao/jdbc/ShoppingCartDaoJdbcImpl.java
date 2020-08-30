package com.internetshop.dao.jdbc;

import com.internetshop.dao.ShoppingCartDao;
import com.internetshop.exceptions.DataProcessingException;
import com.internetshop.lib.Dao;
import com.internetshop.model.Product;
import com.internetshop.model.ShoppingCart;
import com.internetshop.util.ConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;

@Dao
public class ShoppingCartDaoJdbcImpl implements ShoppingCartDao {
    private static final int ID_COLUMN = 1;
    private static final Logger LOGGER = Logger.getLogger(ShoppingCartDaoJdbcImpl.class);

    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        String query = "SELECT * FROM shopping_carts WHERE user_id = ?;";
        ShoppingCart shoppingCart;
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart = getShoppingCartFromResultSet(resultSet);
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't FIND cart in mySQL internet_shop", ex);
        }
        shoppingCart.setProducts(getProductsFromShoppingCartId(shoppingCart.getId()));
        return Optional.of(shoppingCart);
    }

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts (user_id) VALUES (?);";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query,
                         PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, shoppingCart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            long cartId = resultSet.getLong(ID_COLUMN);
            shoppingCart.setId(cartId);
            insertCartsProducts(shoppingCart);
            LOGGER.info("Successful INSERT cart in mySQL with ID " + cartId);
            return shoppingCart;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't INSERT cart in mySQL internet_shop", ex);
        }
    }

    @Override
    public Optional<ShoppingCart> get(Long id) {
        String query = "SELECT * FROM shopping_carts WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ShoppingCart shoppingCart = getShoppingCartFromResultSet(resultSet);
                shoppingCart.setProducts(getProductsFromShoppingCartId(id));
                return Optional.of(shoppingCart);
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't FIND cart in mySQL internet_shop", ex);
        }
    }

    @Override
    public List<ShoppingCart> getAll() {
        String query = "SELECT * FROM shopping_carts;";
        List<ShoppingCart> allShoppingCarts = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ShoppingCart shoppingCart = getShoppingCartFromResultSet(resultSet);
                shoppingCart.setProducts(getProductsFromShoppingCartId(shoppingCart.getId()));
                allShoppingCarts.add(shoppingCart);
            }
            return allShoppingCarts;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't SELECT carts in mySQL internet_shop", ex);
        }
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        String query = "UPDATE shopping_carts SET user_id = ? "
                + "WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, shoppingCart.getUserId());
            statement.setLong(2, shoppingCart.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't UPDATE cart in mySQL internet_shop", ex);
        }
        deleteShoppingCartFromCartsProducts(shoppingCart.getId());
        insertCartsProducts(shoppingCart);
        LOGGER.info("Shopping cart with ID=" + shoppingCart.getId() + " was UPDATED");
        return shoppingCart;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM shopping_carts WHERE cart_id = ?;";
        deleteShoppingCartFromCartsProducts(id);
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            int numberOfRowsDeleted = statement.executeUpdate();
            return numberOfRowsDeleted != 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't DELETE cart in mySQL internet_shop", ex);
        }
    }

    private void insertCartsProducts(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts_products (cart_id, product_id) "
                + "VALUES (?, ?);";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop()) {
            for (Product product : shoppingCart.getProducts()) {
                PreparedStatement insertStatement =
                        connection.prepareStatement(query);
                insertStatement.setLong(1, shoppingCart.getId());
                insertStatement.setLong(2, product.getId());
                insertStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't INSERT products in cart in "
                    + "mySQL internet_shop", ex);
        }
    }

    private void deleteShoppingCartFromCartsProducts(Long shoppingCartId) {
        String query = "DELETE FROM shopping_carts_products WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, shoppingCartId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't DELETE products from cart in "
                    + "mySQL internet_shop", ex);
        }
    }

    private List<Product> getProductsFromShoppingCartId(Long shoppingCartId) {
        String query = "SELECT p.* FROM shopping_carts_products scp "
                + "JOIN products p ON p.product_id = scp.product_id "
                + "WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, shoppingCartId);
            ResultSet resultSet = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("product_id");
                String name = resultSet.getString("product_name");
                BigDecimal price = resultSet.getBigDecimal("price");
                Product product = new Product(name, price);
                product.setId(id);
                products.add(product);
            }
            return products;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't SELECT products in cart "
                    + "in mySQL internet_shop", ex);
        }
    }

    private ShoppingCart getShoppingCartFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("cart_id");
        Long userId = resultSet.getLong("user_id");
        ShoppingCart shoppingCart = new ShoppingCart(userId);
        shoppingCart.setId(id);
        return shoppingCart;
    }
}
