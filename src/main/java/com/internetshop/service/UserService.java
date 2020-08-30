package com.internetshop.service;

import com.internetshop.model.User;
import java.util.Optional;

public interface UserService extends GenericService<User, Long> {

    Optional<User> getByLogin(String login);
}
