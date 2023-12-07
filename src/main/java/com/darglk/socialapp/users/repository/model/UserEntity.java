package com.darglk.socialapp.users.repository.model;

import java.time.Instant;
import java.util.List;

public record UserEntity(String username, List<String> following, Instant createdAt) {
}
