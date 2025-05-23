package org.example.wecambackend.repos;

import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.User.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInformationRepository extends JpaRepository<UserInformation,Long> {

    Optional<UserInformation> findByUser(User user);
    Optional<UserInformation> findByUser_UserPkId(Long userid);
}
