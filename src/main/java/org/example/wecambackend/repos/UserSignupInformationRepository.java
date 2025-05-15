package org.example.wecambackend.repos;

import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.User.UserSignupInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSignupInformationRepository extends JpaRepository<UserSignupInformation, User> {
}
