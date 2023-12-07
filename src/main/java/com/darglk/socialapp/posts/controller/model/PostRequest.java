package com.darglk.socialapp.posts.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank(message = "cannot be blank")
        String username,
        @NotBlank(message = "cannot be blank")
        @Size(message = "size must be bigger than 0 and less than 140 characters", min = 1, max = 140)
        String content
) {}
