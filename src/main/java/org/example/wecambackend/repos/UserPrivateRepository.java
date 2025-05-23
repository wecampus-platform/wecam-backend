package org.example.wecambackend.repos;

import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.User.UserPrivate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPrivateRepository  extends JpaRepository<UserPrivate, User> {

    @Query("SELECT u.phoneNumber FROM UserPrivate u WHERE u.user.userPkId = :userId")
    Optional<String> findEncryptedPhoneNumberByUserId(@Param("userId") Long userId);
}
