package com.darglk.socialapp.posts.repository.model;

import java.time.Instant;

public record PostEntity(String id, String content, Instant createdAt, String username) {
}
