package com.thearckay.portifolio.entitys;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thearckay.portifolio.dto.ProjectRequest;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String pictureUrl;

    @Column(columnDefinition = "TEXT")
    private String githubRepository;

    @Column(columnDefinition = "TEXT")
    private String deployUrl;

    @Column(nullable = false, name = "project_order")
    private Integer order;

    @ElementCollection
    @CollectionTable(name = "project_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag_name")
    private List<String> tagList= new ArrayList<>();

    public Project(){}
    public Project(User user, String title, String description, String pictureUrl, String githubPepository, String deployUrl, Integer order, List<String> tagList) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.githubRepository = githubPepository;
        this.deployUrl = deployUrl;
        this.order = order;
        this.tagList = tagList;
    }
    public Project(Integer id, User user, String title, String description, String pictureUrl, String githubRepository, String deployUrl, Integer order, List<String> tagList) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.githubRepository = githubRepository;
        this.deployUrl = deployUrl;
        this.order = order;
        this.tagList = tagList;
    }

    public static Project convertProjectRequestInProject(ProjectRequest projectRequest, User userFromContext){
        Project project = new Project();
        project.setOrder(projectRequest.order());
        project.setUser(userFromContext);
        project.setTitle(projectRequest.title());
        project.setDescription(projectRequest.description());
        project.setGithubRepository(projectRequest.githubRepository());
        project.setDeployUrl(projectRequest.deployLink());
        project.setTagList(projectRequest.tagList());
        return project;
    }


    public Boolean addTag(String tagToAdd){
        return getTagList().add(tagToAdd);
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public String getGithubRepository() {
        return githubRepository;
    }
    public void setGithubRepository(String githubPepository) {
        this.githubRepository = githubPepository;
    }
    public String getDeployUrl() {
        return deployUrl;
    }
    public void setDeployUrl(String deployUrl) {
        this.deployUrl = deployUrl;
    }
    public Integer getOrder() {
        return order;
    }
    public void setOrder(Integer order) {
        this.order = order;
    }
    public List<String> getTagList() {
        return tagList;
    }
    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}
