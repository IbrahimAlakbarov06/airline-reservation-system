package org.airline.msuser.domain.repo;

import org.airline.msuser.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByEmail(String email);

    boolean existsByUserId(Long userId);

    boolean existsByEmail(String email);
}
