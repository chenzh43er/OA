package com.shop.service.impl;

import com.shop.mapper.UserMapper;
import com.shop.pojo.User;
import com.shop.pojo.UserExample;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param username
     * @param userPassword
     * @return
     */
    @Override
    public List<User> isLogin(String username, String userPassword) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andUserpasswordEqualTo(userPassword);
        List<User> list = userMapper.selectByExample(userExample);
        return list;
    }

    @Override
    public void regesiterUser(User user) {
        userMapper.insert(user);
    }
}
