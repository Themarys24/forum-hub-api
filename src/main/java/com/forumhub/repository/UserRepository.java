package com.forumhub.repository;

import com.forumhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Method to Spring Security
    UserDetails findByEmail(String email);

    //Check if email already exists
    boolean existsByEmail(String email);

    //Search for active user by email
    Optional<User> findByEmailAndActiveTrue(String email);

    //Search for all active users
    List<User> findByActiveTrue();

    //Search for user by role
    List<User> findByRole(User.Role role);

    //Search for users active by role
    List<User> findByRoleAndActiveTrue(User.Role role);

    //Search for name(case insentive)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) AND u.active = true")
    List<User> findByNameContainingIgnoreCaseAndActiveTrue(@Param("name") String name);
}



