package com.darglk.socialapp.users.service;

import com.darglk.socialapp.users.controller.model.UserResponse;

public interface UsersService {
    void createUser(String username);

    void follow(String follower, String followed);

    UserResponse getUser(String username);
}
