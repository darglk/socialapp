package com.darglk.socialapp.users.controller.model;

import java.util.List;

public record UserResponse(String username, List<String> following) {
}
