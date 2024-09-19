package com.interview.service;

import com.interview.model.User;

import java.util.List;

public interface UserService {
    List<User> createUserAndAdd(final String userId, final String name, final String emailId, final String phoneNumber,
                                List<User> userList);
}
