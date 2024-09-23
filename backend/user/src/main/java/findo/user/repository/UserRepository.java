package findo.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import findo.user.entity.EntityUser;

public interface UserRepository extends JpaRepository<EntityUser, UUID> {
    Optional<EntityUser> findByEmail(String email);
}