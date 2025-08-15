package org.airline.msuser.domain.repo;

import org.airline.msuser.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByEmail(String email);

    boolean existsByUserId(Long userId);

    @Query("select up from UserProfile up where up.isProfileComplete = true")
    List<UserProfile> findCompleteProfiles();

    @Query("select up from UserProfile up where up.isProfileComplete = false")
    List<UserProfile> findIncompleteProfiles();
}
