package com.thearckay.portifolio.service;

import com.thearckay.portifolio.config.CloudinaryConfig;
import com.thearckay.portifolio.dto.ApiResponse;
import com.thearckay.portifolio.dto.ProjectRequest;
import com.thearckay.portifolio.entitys.Project;
import com.thearckay.portifolio.entitys.User;
import com.thearckay.portifolio.exceptions.ProjectException;
import com.thearckay.portifolio.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CloudinaryConfig cloudinaryConfig;

    @Transactional
    public ResponseEntity<ApiResponse> createProject(ProjectRequest projectRequest, MultipartFile file, User userLogged) {
        try {
            Optional<Project> projectOptional = projectRepository.findProjectByTitle(projectRequest.title());
            if (projectOptional.isPresent()) throw new ProjectException("Já existe um projeto com esse titulo!", HttpStatus.CONFLICT.value());

            String imageUrl = cloudinaryConfig.uploadFileAndReturnUrl(file);
            Project newProject = Project.convertProjectRequestInProject(projectRequest, userLogged);
            newProject.setPictureUrl(imageUrl);

            List<Project> projectsList = projectRepository.findProjectsByUser(userLogged);
            this.addProjectAndSortList(newProject, projectsList);

            System.out.println("A url é: "+imageUrl);
            projectRepository.saveAll(projectsList);

            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK.value(),
                    "",
                    "Projeto criado com sucesso!",
                    LocalDateTime.now()
            ));

        } catch (IOException e){
            throw new ProjectException(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }

    }

    public ResponseEntity<ApiResponse> getProjectList(User user) {
        List<Project> projectList = projectRepository.findProjectsByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(
                HttpStatus.OK.value(),
                projectList,
                "Lista Retornada com sucesso!",
                LocalDateTime.now()
        ));
    }

    public ResponseEntity<ApiResponse> getProjectById(Integer projectId) {
        Optional<Project> projectOptional = projectRepository.findProjectById(projectId);
        if (projectOptional.isEmpty()) throw new ProjectException("O id do Projeto é inválido!", HttpStatus.NO_CONTENT.value());
        Project projectToReturn = projectOptional.get();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse (
                HttpStatus.OK.value(),
                projectToReturn,
                "Projeto retornado com sucesso!",
                LocalDateTime.now()
        ));

    }

    public ResponseEntity<ApiResponse> updateProject(Project projectToUpdate) {
        Project projectSaved = projectRepository.save(projectToUpdate);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                projectSaved,
                "Projeto atualizado com sucesso!",
                LocalDateTime.now()
        ));
    }

    public ResponseEntity<ApiResponse> deleteProjectById(Integer projectId, User userLogged) {

        Optional<Project> projectOptional = projectRepository.findProjectById(projectId);
        if (projectOptional.isEmpty()) throw new ProjectException("Id do projeto inválido!", HttpStatus.NO_CONTENT.value());

        Project projectToDelete = projectOptional.get();
        projectRepository.delete(projectToDelete);

        List<Project> projectList = projectRepository.findProjectsByUser(userLogged);

        this.sortProjectList(projectList);
        projectRepository.saveAll(projectList);

        return ResponseEntity.ok().body(new ApiResponse(
                HttpStatus.OK.value(),
                Collections.emptyList(),
                "Projeto deletado com sucesso!",
                LocalDateTime.now()
        ));
    }


    private void addProjectAndSortList(Project projecToBeAdded, List<Project> projectListToSort){
        projectListToSort.add(projecToBeAdded);
        this.sortProjectList(projectListToSort);
    }

    private void sortProjectList(List<Project> projectListToSort){
        projectListToSort.sort(Comparator.comparing(Project::getOrder));
        for (int i = 0; i < projectListToSort.size(); i++) {
            projectListToSort.get(i).setOrder(i + 1);
        }
    }
}
