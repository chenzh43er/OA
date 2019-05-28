package com.shop.service;

import java.util.List;

import com.shop.pojo.User;

public interface UserService {

    List<User> isLogin(String username, String userPassword);

    void regesiterUser(User user);

}
