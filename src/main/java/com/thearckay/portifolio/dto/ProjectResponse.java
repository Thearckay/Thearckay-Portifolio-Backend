package com.thearckay.portifolio.dto;

import java.util.List;

public record ProjectResponse(
    String title,
    String description,
    String pictureUrl,
    String githubRepository,
    String deployUrl,
    List<String> tagList,
    Integer order
) {
}
