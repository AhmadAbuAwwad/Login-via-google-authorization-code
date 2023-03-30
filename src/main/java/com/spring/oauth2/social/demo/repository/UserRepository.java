package com.spring.oauth2.social.demo.repository;

import com.spring.oauth2.social.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByGoogleId(String googleId);
    User findByEmail(String email);
}
