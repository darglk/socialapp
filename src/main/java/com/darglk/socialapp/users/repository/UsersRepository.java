package com.darglk.socialapp.users.repository;

import com.darglk.socialapp.users.repository.model.UserEntity;

import java.util.Optional;

public interface UsersRepository {
    void save(String username);

    void saveFollowed(String follower, String followed);

    Optional<UserEntity> findByUsername(String username);

    void deleteAll();
}
