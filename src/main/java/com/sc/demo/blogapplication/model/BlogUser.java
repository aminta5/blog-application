package com.sc.demo.blogapplication.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
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
@Table(name = "users")
public class BlogUser {

  @Id
  @GeneratedValue
  @Column(
      columnDefinition = "BINARY(16)",
      updatable = false,
      nullable = false,
      unique = true)
  private UUID id;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Instant updatedAt;

  @NotEmpty
  @Column(unique = true, nullable = false, name = "email")
  private String username;

  @Size(min = 8, message = "min 8 characters")
  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,}$", message = "password must contain at least one letter and one number")
  private String password;

  @NotEmpty
  @Column(name = "display_name")
  private String displayName;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts;

}
