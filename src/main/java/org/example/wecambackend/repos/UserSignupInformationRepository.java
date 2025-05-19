package org.example.wecambackend.repos;

import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.User.UserSignupInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSignupInformationRepository extends JpaRepository<UserSignupInformation, User> {
    Optional<UserSignupInformation> findByUser_UserPkId(Long userid);
}
