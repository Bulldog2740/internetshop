package com.internetshop.security;

import com.internetshop.exceptions.AuthenticationException;
import com.internetshop.model.User;

public interface AuthenticationService {
    User login(String login, String password) throws AuthenticationException;
}
