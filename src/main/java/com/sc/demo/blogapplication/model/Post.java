package com.sc.demo.blogapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {

  @Id
  @Column(columnDefinition = "BINARY(16)",
      updatable = false,
      nullable = false,
      name = "Id",
      unique = true)
  @GeneratedValue
  private UUID id;

  @NotEmpty(message = "Post title cannot be empty")
  private String title;
  @Lob
  @NotEmpty(message = "Post content cannot be empty")
  private String content;

  @ElementCollection
  @Builder.Default
  private Set<String> tags = new HashSet<>();

  @PrePersist
  public void prePersist() {
    id = id == null ? UUID.randomUUID() : id;
  }
}
