package com.example.identity.oktaidentityservice.service;

import com.example.identity.oktaidentityservice.api.model.UserDetailsModel;
//import com.example.identity.oktaidentityservice.repository.UserRepository;
import com.example.identity.oktaidentityservice.api.model.UserIdentityModel;
import com.example.identity.oktaidentityservice.api.model.UserModel;
import com.example.identity.oktaidentityservice.entity.UserEntity;
import com.example.identity.oktaidentityservice.repository.UserRepository;
import com.okta.sdk.resource.api.UserApi;
import com.okta.sdk.resource.model.UpdateUserRequest;
import com.okta.sdk.resource.model.User;
import com.okta.sdk.resource.model.UserProfile;
import com.okta.sdk.resource.user.UserBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.identity.oktaidentityservice.util.AppConstants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserManagementService {

    private final UserApi userApi;
    private final UserRepository userRepository;

//    private final UserRepository userRepository;

    @Value("${identity.app.role-name}")
    private String appRoleName;

    public List<UserDetailsModel> searchUsers(String userName) {
        log.info("Searching data for userName: {}", userName);
        List<User> users = listUsers(userName);
        return users.stream().map(this::toUserDetailsModel).toList();
    }

    private List<User> listUsers(String userName) {
        List<User> users = userApi.listUsers(null, null, 200, null,
                "profile.login sw \"" + userName + "\"", "profile.login", "asc");
        return users;
    }

    private UserDetailsModel toUserDetailsModel(User user) {
//        assert user.getProfile() != null;
        return UserDetailsModel.builder()
                .userid(user.getId())
                .firstname(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .email(user.getProfile().getEmail())
                .userName(user.getProfile().getLogin())
                .created(user.getCreated())
                .activated(user.getActivated())
                .statusChanged(user.getStatusChanged())
                .lastLogin(user.getLastLogin())
                .lastUpdated(user.getLastUpdated())
                .passwordChanged(user.getPasswordChanged())
                .status(user.getStatus())
                .build();
    }

    public void createUser(UserIdentityModel userModel) {
        log.info("Creating User: {}", userModel);

        var createdUser = UserBuilder.instance()
                .setFirstName(userModel.getFirstName())
                .setLastName(userModel.getLastName())
                .setEmail(userModel.getEmail())
                .setLogin(userModel.getUserName())
                .setCustomProfileProperty(APP_ROLE_KEY, List.of(appRoleName))
                .setCustomProfileProperty(CBC_ID_KEY, userModel.getUserName())
                .setCustomProfileProperty(CIS_NUMBERS_KEY, List.of(userModel.getCustomerNumber()))
                .setCustomProfileProperty(CUSTOMER_CODE_KEY, StringUtils.substring(userModel.getUserName(), 0, 8))
                .buildAndCreate(userApi);

    }

    public void updateUser(String userName, UserIdentityModel userModel) {
        log.info("Updating userName: {}", userName);
        var user = getUser(userName);

        var userProfile = user.getProfile();
        if(StringUtils.isNotBlank(userModel.getFirstName())) {
            userProfile.setFirstName(userModel.getFirstName());
        }

        if(StringUtils.isNotBlank(userModel.getLastName())) {
            userProfile.setLastName(userModel.getLastName());
        }

        if(StringUtils.isNotBlank(userModel.getEmail())) {
            userProfile.setEmail(userModel.getEmail());
        }

        if(StringUtils.isNotBlank(userModel.getCustomerNumber())) {
            Map<String, Object> additionalProperties = userProfile.getAdditionalProperties();
            additionalProperties.put(CIS_NUMBERS_KEY, List.of(userModel.getCustomerNumber()));
        }

        User updatedUser = updateUserProfile(user.getId(), userProfile);

        userModel.setOktaId(updatedUser.getId());
        userModel.setCreatedTime(new Date());

        var userIdentityModel = saveUserToDB(userModel);
        log.info("Updated UserProfile for userName: {} and updateUser: {}", userName, userIdentityModel);

    }

    @Transactional
    public UserIdentityModel saveUserToDB(UserIdentityModel userModel) {
        UserEntity newUserEntity = convertToUserEntity(userModel);
        Optional<UserEntity> existingUserEntity = userRepository.findByUserName(newUserEntity.getUserName());
        if(existingUserEntity.isPresent()) {
            UserEntity existingUser = existingUserEntity.get();
            existingUser.setEmail(newUserEntity.getEmail());
            existingUser.setFirstName(newUserEntity.getFirstName());
            existingUser.setLastName(newUserEntity.getLastName());
            existingUser.setOktaId(newUserEntity.getOktaId());
            existingUser.setCustomerNumber(newUserEntity.getCustomerNumber());
            existingUser.setModifiedBy(newUserEntity.getModifiedBy());
            existingUser.setModifiedTime(new Date());
            log.info("Updating identity user: {}", existingUser);
            return convertToUserModel(userRepository.save(existingUser));
        }
        return convertToUserModel(userRepository.save(newUserEntity));
    }

    private UserIdentityModel convertToUserModel(UserEntity userEntity) {
        var createdUser = getUser(userEntity.getCreatedBy());
        UserIdentityModel userIdentityModel = new UserIdentityModel();
        BeanUtils.copyProperties(userEntity, userIdentityModel);
        userIdentityModel.setCreatedBy(toUserModel(createdUser));
        return userIdentityModel;
    }

    private UserModel toUserModel(User createdUser) {
        UserModel userModel = new UserModel();
        userModel.setFirstName(createdUser.getProfile().getFirstName());
        userModel.setLastName(createdUser.getProfile().getLastName());
        userModel.setEmail(createdUser.getProfile().getEmail());
        userModel.setUserName(createdUser.getProfile().getLogin());
        return userModel;
    }

//    private @NotNull(message = "createdBy is required") @Valid UserModel toUserModel(User createdUser) {
//        return null;
//    }

    private UserEntity convertToUserEntity(UserIdentityModel userIdentityModel) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userIdentityModel, userEntity);
        userEntity.setCreatedBy(userIdentityModel.getCreatedBy().getEmail());
        userEntity.setCreatedTime(new Date());
        userEntity.setModifiedTime(new Date());
        userEntity.setModifiedBy(userIdentityModel.getModifiedBy());
        return  userEntity;
    }

    private User updateUserProfile(String id, UserProfile userProfile) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setProfile(userProfile);
        return userApi.updateUser(id, updateUserRequest, Boolean.TRUE);
    }

    private User getUser(String userName) {
        log.info("Retrieving userName: {}", userName);
        User user = userApi.getUser(userName);
        log.info("Retrieving userName: {}", user);
        return user;
    }
}
