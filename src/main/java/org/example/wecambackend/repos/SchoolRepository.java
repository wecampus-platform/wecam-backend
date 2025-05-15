package org.example.wecambackend.repos;


import org.example.wecambackend.model.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<University, Long> {
}