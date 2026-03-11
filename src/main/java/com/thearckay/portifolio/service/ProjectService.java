package com.thearckay.portifolio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thearckay.portifolio.config.CloudinaryConfig;
import com.thearckay.portifolio.dto.ApiResponse;
import com.thearckay.portifolio.dto.ProjectRequest;
import com.thearckay.portifolio.entitys.Project;
import com.thearckay.portifolio.entitys.Tag;
import com.thearckay.portifolio.entitys.User;
import com.thearckay.portifolio.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CloudinaryConfig cloudinaryConfig;

    @Transactional
    public ResponseEntity<ApiResponse> createProject(ProjectRequest projectRequest, MultipartFile file, User userLogged){
        try {
            Cloudinary cloudinary = cloudinaryConfig.cloudinary();
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = uploadResult.get("secure_url").toString();

            Project newProject = Project.convertProjectRequestInProject(projectRequest, userLogged);
            List<Tag> tagList = projectRequest.tagList().stream().map(tagName -> new Tag(newProject, tagName)).toList();

            newProject.setTagList(tagList);
            newProject.setPictureUrl(imageUrl);

            System.out.println("A url é: "+imageUrl);
            projectRepository.save(newProject);

            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK.value(),
                    "",
                    "Projeto criado com sucesso!",
                    LocalDateTime.now()
            ));

        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
