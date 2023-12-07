package com.darglk.socialapp.posts.controller.model;

import java.time.Instant;

public record PostResponse(String id, String content, Instant createdAt, String username) {
}
