package com.thearckay.portifolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProjectRequest(
        String title,
        String description,
        Integer order,
        @JsonProperty("tag_list")
        List<String> tagList,
        @JsonProperty("github_repository")
        String githubRepository,
        @JsonProperty("deploy_link")
        String deployLink
) {
}
