package com.example.identity.oktaidentityservice.api.handler;

import com.example.identity.oktaidentityservice.api.model.UserDetailsModel;
import com.example.identity.oktaidentityservice.api.model.UserIdentityModel;
import com.example.identity.oktaidentityservice.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.identity.oktaidentityservice.util.AppConstants.USER_NAME;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserManagementController {
    private final UserManagementService userManagementService;

    @GetMapping("/users/{userName}")
    public List<UserDetailsModel> getUsers(@PathVariable(name = USER_NAME) String userName) {
        log.info("UserManagementController -> getUsers: {}", userName);

        return userManagementService.searchUsers(userName);
    }

    @PostMapping("/users")
    public void createUser(@RequestBody @Valid UserIdentityModel userModel) {
        log.info("UserManagementController -> createUser: {}", userModel);

        userManagementService.createUser(userModel);
    }

    @PostMapping("/users/{userName}")
    public void updateUser(@RequestBody @Valid UserIdentityModel userModel,
                           @PathVariable(name = USER_NAME) String userName) {
        log.info("UserManagementController -> updateUser: {}, userName: {} ", userModel, userName);

        userManagementService.updateUser(userName, userModel);
    }
}
