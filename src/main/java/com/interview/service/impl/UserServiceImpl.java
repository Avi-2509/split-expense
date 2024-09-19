package com.interview.service.impl;

import com.interview.model.User;
import com.interview.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public List<User> createUserAndAdd(String userId, String name, String emailId, String phoneNumber, List<User> userList) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(emailId);
        user.setPhoneNumber(phoneNumber);
        userList.add(user);
        return userList;
    }
}
