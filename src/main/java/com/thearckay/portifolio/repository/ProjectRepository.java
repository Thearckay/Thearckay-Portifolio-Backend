package com.thearckay.portifolio.repository;

import com.thearckay.portifolio.entitys.Project;
import com.thearckay.portifolio.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Modifying
    @Query("UPDATE Project p SET p.order = p.order + 1 WHERE p.user = :user AND p.order >= :targetOrder")
    void shiftOrdersForward(User user, Integer targetOrder);

    long countByUser(User user);
}
