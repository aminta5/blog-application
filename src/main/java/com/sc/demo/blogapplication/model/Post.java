package com.sc.demo.blogapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
      unique = true)
  @GeneratedValue
  private UUID id;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Instant updatedAt;

  @NotEmpty(message = "Post title cannot be empty")
  private String title;
  @Lob
  @NotEmpty(message = "Post content cannot be empty")
  private String content;

  @ElementCollection
  @Builder.Default
  private Set<String> tags = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private BlogUser user;


}
