package com.darglk.socialapp.users.controller.model;

import jakarta.validation.constraints.NotBlank;

public record FollowRequest(
        @NotBlank(message = "cannot be empty") String follower,
        @NotBlank(message = "cannot be empty") String followed
) {}
