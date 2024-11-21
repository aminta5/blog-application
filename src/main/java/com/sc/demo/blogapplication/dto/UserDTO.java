package com.sc.demo.blogapplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDTO(@NotEmpty(message = "username cannot be empty") String username,
                      @Size(min = 8, message = "password must be at least 8 characters long")
                      @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,}$", message = "password must contain at least one letter and one number")
                      String password,
                      @NotEmpty(message = "display name cannot be empty") String displayName) {

}
