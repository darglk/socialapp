package com.darglk.socialapp.users.service;

import com.darglk.socialapp.exception.BadRequestException;
import com.darglk.socialapp.exception.NotFoundException;
import com.darglk.socialapp.users.controller.model.UserResponse;
import com.darglk.socialapp.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public void createUser(String username) {
        usersRepository.save(username);
    }

    @Override
    public void follow(String follower, String followed) {
        if (follower.equals(followed)) {
            throw new BadRequestException("You cannot follow yourself");
        }
        if (usersRepository.findByUsername(followed).isEmpty()) {
            throw new NotFoundException(String.format("Username %s was not found", followed));
        }
        var followerOpt = usersRepository.findByUsername(follower);
        if (followerOpt.isEmpty()) {
            throw new NotFoundException(String.format("Username %s was not found", follower));
        }
        if (followerOpt.get().following().contains(followed)) {
            throw new BadRequestException("You already follow this user");
        }
        usersRepository.saveFollowed(follower, followed);
    }

    @Override
    public UserResponse getUser(String username) {
        var user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("Username %s was not found", username)));
        return new UserResponse(user.username(), user.following());
    }
}
