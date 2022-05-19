package com.example.growingshop.domain.user.repository;

import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {
    Optional<User> findUsersByLoginIdAndPassword(String loginId, String password);
}
