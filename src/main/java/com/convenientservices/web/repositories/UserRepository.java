package com.convenientservices.web.repositories;

import com.convenientservices.web.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String login);
    User findFirstByActivationCode(String activateCode);
}