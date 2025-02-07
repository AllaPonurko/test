package org.example.repository;

import org.example.entity.user.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "select user from User user where user.email=:email ")
    Optional<User> findByEmail(@NotNull@Param("email") String email);

    @NotNull
    Optional<User> findById(@NotNull UUID id);
}
