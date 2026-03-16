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

import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/frontend")
    public ResponseEntity<ApiResponse> getProjectForFrontend(){
        return projectService.getProjectsForFrontend();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createProject(
            @RequestPart("project") ProjectRequest projectRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user)
    {
        System.out.println("controller de criação chamado");
        return projectService.createProject(projectRequest, file, user);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getProjectList(@AuthenticationPrincipal User userLogged){
        return projectService.getProjectList(userLogged);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProjectToUpdate(@PathVariable("id") UUID projectId){
        return projectService.getProjectByIdToUpdate(projectId);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/{id}")
    public ResponseEntity<ApiResponse> updateProject(
            @PathVariable("id") UUID id,
            @RequestPart("project") Project projectToUpdate,
            @RequestPart(name = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws Exception {
        return projectService.updateProjectInformations(projectToUpdate, id, file, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable("id") UUID projectId, @AuthenticationPrincipal User userLogged){
        return projectService.deleteProjectById(projectId, userLogged);
    }

    @DeleteMapping("/image/{id}")
    public ResponseEntity<ApiResponse> deleteImageProject(@PathVariable("id") UUID projectId){
        return projectService.deleteProjectImageByProjectId(projectId);
    }

}
