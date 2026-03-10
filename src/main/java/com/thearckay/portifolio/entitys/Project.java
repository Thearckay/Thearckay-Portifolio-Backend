package com.thearckay.portifolio.entitys;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

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
    private String githubPepository;

    @Column(columnDefinition = "TEXT")
    private String deployUrl;

    @Column(nullable = false, name = "project_order")
    private Integer order;

    @OneToMany(mappedBy = "project")
    private List<Tag> tagList;

    public Project(){}
    public Project(User user, String title, String description, String pictureUrl, String githubPepository, String deployUrl, Integer order, List<Tag> tagList) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.githubPepository = githubPepository;
        this.deployUrl = deployUrl;
        this.order = order;
        this.tagList = tagList;
    }
    public Project(Integer id, User user, String title, String description, String pictureUrl, String githubPepository, String deployUrl, Integer order, List<Tag> tagList) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.githubPepository = githubPepository;
        this.deployUrl = deployUrl;
        this.order = order;
        this.tagList = tagList;
    }

    public Boolean addTag(Tag tagToAdd){
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
    public String getGithubPepository() {
        return githubPepository;
    }
    public void setGithubPepository(String githubPepository) {
        this.githubPepository = githubPepository;
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
    public List<Tag> getTagList() {
        return tagList;
    }
    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }
}
