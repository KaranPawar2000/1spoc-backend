package com._spoc.demo.repository;


import com._spoc.demo.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByPhone(String phone);
}
