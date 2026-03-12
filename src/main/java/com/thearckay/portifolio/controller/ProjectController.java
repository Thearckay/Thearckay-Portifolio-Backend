package com.thearckay.portifolio.controller;

import com.thearckay.portifolio.dto.ApiResponse;
import com.thearckay.portifolio.dto.ProjectRequest;
import com.thearckay.portifolio.entitys.Project;
import com.thearckay.portifolio.entitys.User;
import com.thearckay.portifolio.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createProject(
            @RequestPart("project") ProjectRequest projectRequest,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal User user)
    {
        return projectService.createProject(projectRequest, file, user);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProjectList(@AuthenticationPrincipal User userLogged){
        return projectService.getProjectList(userLogged);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProject(@PathVariable("id") Integer projectId){
        return projectService.getProjectById(projectId);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateProject(@RequestBody Project projectToUpdate){
        return projectService.updateProject(projectToUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProjecr(@PathVariable("id") Integer projectId, @AuthenticationPrincipal User userLogged){
        return projectService.deleteProjectById(projectId, userLogged);
    }

}
