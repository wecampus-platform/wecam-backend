package org.example.wecambackend.repos;

import org.example.wecambackend.model.User.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInformationRepository extends JpaRepository<UserInformation,Long> {

}
