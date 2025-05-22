package org.example.wecambackend.repos;


import org.example.wecambackend.model.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<University, Long> {
    Optional<University> findBySchoolId(Long schoolId);
}