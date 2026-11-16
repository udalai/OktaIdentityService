package com.example.identity.oktaidentityservice.repository;

import com.example.identity.oktaidentityservice.entity.UserLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, String> {
    List<UserLoginEntity> findTop2ByUserNameOrderByLoginTimeDesc(String userName);
}
