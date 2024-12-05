package com.sc.demo.blogapplication.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;

public record PostDTO(@NotEmpty(message = "Title cannot be empty")String title,
                      @NotEmpty(message = "Content cannot be empty")String content,
                     @NotEmpty UUID userId) {

}
