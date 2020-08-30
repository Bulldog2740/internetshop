package com.internetshop.dao.jdbc;

import com.internetshop.dao.ProductDao;
import com.internetshop.exceptions.DataProcessingException;
import com.internetshop.lib.Dao;
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
public class ProductDaoJdbcImpl implements ProductDao {
    private static final int ID_COLUMN = 1;
    private static final Logger LOGGER = Logger.getLogger(ProductDaoJdbcImpl.class);

    @Override
    public Product create(Product product) {
        String query = "INSERT INTO products"
                + " (product_name, price) VALUES (?, ?);";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection
                        .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            Long productId = resultSet.getLong(ID_COLUMN);
            product.setId(productId);
            LOGGER.info("Successful INSERT product in mySQL with ID " + productId);
            return product;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't INSERT product in mySQL internet_shop", ex);
        }
    }

    @Override
    public Optional<Product> get(Long id) {
        String query = "SELECT * FROM products WHERE product_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Product product = getProductFromResultSet(resultSet);
                return Optional.of(product);
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't FIND product by ID "
                    + id + " in mySQL internet_shop", ex);
        }
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM products;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = getProductFromResultSet(resultSet);
                products.add(product);
            }
            return products;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't GET products in mySQL internet_shop", ex);
        }
    }

    @Override
    public Product update(Product product) {
        String query = "UPDATE products "
                + "SET product_name = ?, price = ? "
                + "WHERE product_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setLong(3, product.getId());
            statement.executeUpdate();
            LOGGER.info("Product with ID=" + product.getId() + " was UPDATED");
            return product;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't UPDATE product by ID "
                    + product.getId()
                    + "in mySQL internet_shop", ex);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE  FROM products "
                + "WHERE product_id=?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            int numberOfRowsDeleted = statement.executeUpdate();
            return numberOfRowsDeleted != 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't DELETE product by ID " + id
                    + "in mySQL internet_shop", ex);
        }
    }

    private Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("product_id");
        String name = resultSet.getString("product_name");
        BigDecimal price = resultSet.getBigDecimal("price");
        Product product = new Product(name, price);
        product.setId(id);
        return product;
    }
}
