package com.internetshop.security;

import com.internetshop.exceptions.AuthenticationException;
import com.internetshop.lib.Inject;
import com.internetshop.lib.Service;
import com.internetshop.model.User;
import com.internetshop.service.UserService;
import com.internetshop.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    private UserService userService;

    @Override
    public User login(String login, String password) throws AuthenticationException {
        return userService.getByLogin(login)
                .filter(u -> HashUtil.hashPassword(password, u.getSalt()).equals(u.getPassword()))
                .orElseThrow(() -> new AuthenticationException("Incorrect username or password"));
    }
}
