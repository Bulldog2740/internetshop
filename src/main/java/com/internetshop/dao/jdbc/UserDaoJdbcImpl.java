package com.internetshop.dao.jdbc;

import com.internetshop.dao.UserDao;
import com.internetshop.exceptions.DataProcessingException;
import com.internetshop.lib.Dao;
import com.internetshop.model.Role;
import com.internetshop.model.User;
import com.internetshop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.log4j.Logger;

@Dao
public class UserDaoJdbcImpl implements UserDao {
    private static final int ID_COLUMN = 1;
    private static final Logger LOGGER = Logger.getLogger(UserDaoJdbcImpl.class);

    @Override
    public Optional<User> getByLogin(String login) {
        String query = "SELECT * FROM users WHERE login = ?;";
        User user;
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't FIND user in mySQL internet_shop", ex);
        }
        user.setRoles(getRolesFromUserId(user.getId()));
        return Optional.of(user);
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users (user_name, login, pass, salt) "
                + "VALUES (?, ?, ?, ?);";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query,
                         PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, user.getSalt());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            long id = resultSet.getLong(ID_COLUMN);
            user.setId(id);
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't INSERT user in mySQL internet_shop", ex);
        }
        insertUsersRoles(user);
        LOGGER.info("Successful INSERT user in mySQL with ID " + user.getId());
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        String query = "SELECT * FROM users WHERE user_id = ?;";
        User user;
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't FIND user in mySQL internet_shop", ex);
        }
        user.setRoles(getRolesFromUserId(id));
        return Optional.of(user);
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM users;";
        List<User> allUsers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = getUserFromResultSet(resultSet);
                user.setRoles(getRolesFromUserId(user.getId()));
                allUsers.add(user);
            }
            return allUsers;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't SELECT users in mySQL internet_shop", ex);
        }
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET user_name = ?, login = ?, pass = ?, salt = ? "
                + "WHERE user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, user.getSalt());
            statement.setLong(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't UPDATE user in mySQL internet_shop", ex);
        }
        deleteUserFromUsersRoles(user.getId());
        insertUsersRoles(user);
        LOGGER.info("Shopping user with ID=" + user.getId() + " was UPDATED");
        return user;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM users WHERE user_id = ?;";
        deleteUserFromUsersRoles(id);
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            int numberOfRowsDeleted = statement.executeUpdate();
            LOGGER.info("User with id " + id + " was deleted.");
            return numberOfRowsDeleted != 0;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't DELETE user in mySQL internet_shop", ex);
        }
    }

    private void insertUsersRoles(User user) {
        String selectRoleIdQuery = "SELECT role_id FROM roles WHERE role_name = ?";
        String insertUsersRolesQuery = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?);";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop()) {
            for (Role role : user.getRoles()) {
                PreparedStatement selectStatement =
                        connection.prepareStatement(selectRoleIdQuery);
                selectStatement.setString(1, role.getRoleName().name());
                ResultSet resultSet = selectStatement.executeQuery();
                resultSet.next();
                PreparedStatement insertStatement =
                        connection.prepareStatement(insertUsersRolesQuery);
                insertStatement.setLong(1, user.getId());
                insertStatement.setLong(2, resultSet.getLong("role_id"));
                insertStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't INSERT user roles in mySQL internet_shop", ex);
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("user_id");
        String name = resultSet.getString("user_name");
        String login = resultSet.getString("login");
        String password = resultSet.getString("pass");
        byte[] salt = resultSet.getBytes("salt");
        User user = new User(name, login, password);
        user.setSalt(salt);
        user.setId(id);
        return user;
    }

    private Set<Role> getRolesFromUserId(Long userId) {
        String query = "SELECT role_name FROM users_roles "
                + "JOIN roles USING (role_id) WHERE user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            Set<Role> roles = new HashSet<>();
            while (resultSet.next()) {
                roles.add(Role.of(resultSet.getString("role_name")));
            }
            return roles;
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't SELECT users roles in "
                    + "mySQL internet_shop", ex);
        }
    }

    private void deleteUserFromUsersRoles(Long userId) {
        String query = "DELETE FROM users_roles WHERE user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnectionInternetShop();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataProcessingException("Can't DELETE users from roles "
                    + "in mySQL internet_shop", ex);
        }
    }
}
