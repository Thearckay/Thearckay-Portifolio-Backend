package com.thearckay.portifolio.service;

import com.thearckay.portifolio.config.CloudinaryConfig;
import com.thearckay.portifolio.dto.ApiResponse;
import com.thearckay.portifolio.dto.ProjectRequest;
import com.thearckay.portifolio.dto.ProjectResponse;
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
import java.util.*;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CloudinaryConfig cloudinaryConfig;

    public ResponseEntity<ApiResponse> getProjectsForFrontend(){
        List<Project> projectList = projectRepository.findAll();

        List<ProjectResponse> projectResponseList = projectList.stream()
                .map(project -> new ProjectResponse(
                        project.getTitle(),
                        project.getDescription(),
                        project.getPictureUrl(),
                        project.getGithubRepository(),
                        project.getDeployUrl(),
                        project.getTagList(),
                        project.getOrder()
                )).toList();


        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                projectResponseList,
                "Projetos retornados com sucesso!",
                LocalDateTime.now()
        ));
    }

    @Transactional
    public ResponseEntity<ApiResponse> createProject(ProjectRequest projectRequest, MultipartFile file, User userLogged) {
        try {
            Optional<Project> projectOptional = projectRepository.findProjectByTitle(projectRequest.title());
            if (projectOptional.isPresent()) throw new ProjectException("Já existe um projeto com esse titulo!", HttpStatus.CONFLICT.value());

            String imageUrl = "https://res.cloudinary.com/dvz6c7kzx/image/upload/v1773502140/loginImagemBackground_uo0x7j.avif";
            String imagePulicId = "";
            Map uploadResult = Map.of();

            if (file != null && !file.isEmpty()) {
                uploadResult = cloudinaryConfig.uploadFile(file);
                imagePulicId = uploadResult.get("public_id").toString();
                imageUrl = uploadResult.get("url").toString();
            }

            Project newProject = Project.convertProjectRequestInProject(projectRequest, userLogged);

            newProject.setPictureUrl(imageUrl);
            newProject.setImagePublicId(imagePulicId);
            newProject.setOrder(projectRequest.order() != null ? projectRequest.order() : 0);

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
                "Lista carregada com sucesso!",
                LocalDateTime.now()
        ));
    }

    public ResponseEntity<ApiResponse> getProjectByIdToUpdate(UUID projectId) {
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

    public ResponseEntity<ApiResponse> updateProjectInformations(Project project, UUID id, MultipartFile file, User userLogged) throws Exception {

        Project projectToUpdate = projectRepository.findProjectById(id)
                .orElseThrow(() -> new ProjectException("Id inválido!", HttpStatus.NO_CONTENT.value()));

        projectToUpdate.setTitle(project.getTitle());
        projectToUpdate.setDescription(project.getDescription());
        projectToUpdate.setGithubRepository(project.getGithubRepository());
        projectToUpdate.setDeployUrl(project.getDeployUrl());
        projectToUpdate.setTagList(project.getTagList());
        projectToUpdate.setOrder(project.getOrder());
        projectToUpdate.setPictureUrl(project.getPictureUrl());

        if (file != null && !file.isEmpty()) {
            if (projectToUpdate.getImagePublicId() != null && !projectToUpdate.getImagePublicId().equals("")){
                Map infosFromOldImage = cloudinaryConfig.cloudinary().api().resource(projectToUpdate.getImagePublicId(), Collections.emptyMap());
                String oldResourceTypeToDelete = infosFromOldImage.get("resource_type").toString();
                cloudinaryConfig.deleteFileByPublicId(projectToUpdate.getImagePublicId(), oldResourceTypeToDelete );
            }
            String newImageUrl = cloudinaryConfig.uploadFileAndReturnUrl(file);
            projectToUpdate.setPictureUrl(newImageUrl);
        }

        Project projectUpdated = projectRepository.save(projectToUpdate);
        List<Project> projectListToSort = projectRepository.findProjectsByUser(userLogged);
        if (!projectListToSort.isEmpty()) this.sortProjectList(projectListToSort);
        projectRepository.saveAll(projectListToSort);

        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                projectUpdated,
                "Projeto atualizado com sucesso!",
                LocalDateTime.now()
        ));
    }

    public ResponseEntity<ApiResponse> deleteProjectImageByProjectId(UUID projectId){

        Project projectToDeleteImage = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectException("Id do Projeto Inválido", HttpStatus.BAD_REQUEST.value()));

        String projectImageId = projectToDeleteImage.getImagePublicId();
        if (projectImageId == null || projectImageId.equals("")) throw new ProjectException("O Projeto não possui imagem!", HttpStatus.NO_CONTENT.value());

        try {
            cloudinaryConfig.deleteImageByPublicId(projectImageId);
            projectToDeleteImage.setImagePublicId("");
            projectToDeleteImage.setPictureUrl("");
        } catch (IOException e){
            throw new ProjectException("Erro ao deletar a imagem do projeto!", HttpStatus.BAD_REQUEST.value());
        }

        Project projectSaved = projectRepository.save(projectToDeleteImage);

        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK.value(),
                projectSaved,
                "Imagem deletada com sucesso!",
                LocalDateTime.now()
        ));

    }


    public ResponseEntity<ApiResponse> deleteProjectById(UUID projectId, User userLogged) {

        Project projectToDelete = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectException("Id do projeto inválido!", HttpStatus.NO_CONTENT.value()));

        if (projectToDelete.getImagePublicId() != null && !projectToDelete.getImagePublicId().equals("")){
            try {
                cloudinaryConfig.deleteImageByPublicId(projectToDelete.getImagePublicId());
            } catch (IOException e) {
                throw new ProjectException("Erro ao deletar a imagem do projeto", HttpStatus.BAD_REQUEST.value());
            }
        }

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
