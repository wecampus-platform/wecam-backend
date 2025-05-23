package org.example.wecambackend.repos;

import org.example.wecambackend.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.userPrivate WHERE u.email = :email")
    Optional<User> findByEmailWithPrivate(@Param("email") String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.organization WHERE u.userPkId = :id")
    Optional<User> findByIdWithOrganization(@Param("id") Long id);

}