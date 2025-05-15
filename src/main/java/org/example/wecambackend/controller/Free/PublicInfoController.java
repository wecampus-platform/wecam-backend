package org.example.wecambackend.controller.Free;

import lombok.RequiredArgsConstructor;
import org.example.wecambackend.dto.responseDTO.OrganizationSimpleResponse;
import org.example.wecambackend.dto.responseDTO.UniversitySimpleResponse;
import org.example.wecambackend.model.Organization;
import org.example.wecambackend.repos.OrganizationRepository;
import org.example.wecambackend.service.client.PublicInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicInfoController {

    private final PublicInfoService publicInfoService;

    @GetMapping("/schools")
    public List<UniversitySimpleResponse> getAllSchools() {
        return publicInfoService.getAllApprovedSchools();
    }

    @GetMapping("/schools/{schoolId}/organizations")
    public List<OrganizationSimpleResponse> getTopLevelOrganizations(
            @PathVariable Long schoolId,
            @RequestParam int level){
        return publicInfoService.getOrganizationsBySchoolAndLevel(schoolId,level);
    }

    @GetMapping("/organizations/{parentId}/children")
    public List<OrganizationSimpleResponse> getChildOrganizations(@PathVariable Long parentId) {
        return publicInfoService.getChildOrganizations(parentId);
    }
}
