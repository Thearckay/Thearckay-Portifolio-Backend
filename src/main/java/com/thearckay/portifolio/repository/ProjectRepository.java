package com.thearckay.portifolio.repository;

import com.thearckay.portifolio.entitys.Project;
import com.thearckay.portifolio.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findProjectsByUser(User user);
    Optional<Project> findProjectByTitle(String title);
    Optional<Project> findProjectById(UUID id);
}
