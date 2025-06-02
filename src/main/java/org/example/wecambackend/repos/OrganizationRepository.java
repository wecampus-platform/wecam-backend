package org.example.wecambackend.repos;

import org.example.wecambackend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByUniversity_SchoolIdAndLevel(Long schoolId, int level);
    List<Organization> findByParent_OrganizationId(Long parentId);

    Optional<Organization> findByOrganizationId(Long organizationId);

    //해당 schoolId와 관련있는 Organization 추출
    Optional<Organization> findOrganizationByUniversity_SchoolIdAndParentIsNull(Long SchoolId);
}
