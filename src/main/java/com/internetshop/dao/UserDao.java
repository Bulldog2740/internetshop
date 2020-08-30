package com.internetshop.dao;

import com.internetshop.model.User;
import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {
    Optional<User> getByLogin(String login);
}
