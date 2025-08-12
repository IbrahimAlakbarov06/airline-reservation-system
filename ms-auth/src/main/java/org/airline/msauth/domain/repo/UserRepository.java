package org.airline.msauth.domain.repo;

import org.airline.msauth.domain.entity.User;
import org.airline.msauth.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select u from User u where u.email = :email and u.isActive = true")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

    @Query("select u from User u where u.isActive = true")
    List<User> findActiveUsers();

    @Query("select u from User u where u.isActive = false ")
    List<User> findInActiveUsers();

    List<User> findByUserRole(UserRole userRole);
}
