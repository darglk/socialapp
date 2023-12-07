package com.darglk.socialapp.users.controller;

import com.darglk.socialapp.users.controller.model.FollowRequest;
import com.darglk.socialapp.users.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @Operation(summary = "Follows specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User now follows another user"),
            @ApiResponse(responseCode = "400", description = "When user tries to follow himself or user already follows specified user"),
            @ApiResponse(responseCode = "404", description = "Follower or followed user is not found")}
    )
    @PostMapping("/follow")
    public void follow(@RequestBody FollowRequest request) {
        usersService.follow(request.follower(), request.followed());
    }
}
