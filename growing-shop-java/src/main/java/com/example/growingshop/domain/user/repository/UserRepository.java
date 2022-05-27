package com.example.growingshop.domain.user.repository;

import com.example.growingshop.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByLoginId(String loginId);
}
