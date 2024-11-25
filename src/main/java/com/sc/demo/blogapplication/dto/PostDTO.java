package com.sc.demo.blogapplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PostDTO(@NotEmpty(message = "Title cannot be empty")String title,
                      @NotEmpty(message = "Content cannot be empty")String content,
                      @NotNull(message = "user_id cannot be empty") UUID userId) {

}
