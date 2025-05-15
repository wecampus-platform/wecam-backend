package org.example.wecambackend.repos;

import org.example.wecambackend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByUniversity_SchoolIdAndLevel(Long schoolId, int level);
    List<Organization> findByParent_OrganizationId(Long parentId);
}
