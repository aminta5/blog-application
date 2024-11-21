package com.sc.demo.blogapplication.dto;

import jakarta.validation.constraints.NotEmpty;

public record PostDTO(@NotEmpty(message = "Title cannot be empty")String title,
                      @NotEmpty(message = "Content cannot be empty")String content) {

}
