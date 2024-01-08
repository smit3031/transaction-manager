package com.example.repository;

import com.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link UserEntity} entities.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Retrieves a user by their ID.
     *
     * @param user_id The ID of the user.
     * @return The user with the specified ID, or null if none is found.
     */
    UserEntity findByUserId(Long user_id);
}
