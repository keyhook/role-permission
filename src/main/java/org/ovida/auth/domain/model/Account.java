package org.ovida.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = "password")
@Entity
@Table(name = "account")
public class Account {

  @Id
  @Setter(AccessLevel.NONE)
  private UUID id = UUID.randomUUID();

  private String email;

  @JsonIgnore
  private String password;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String roleName;

  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Setter(AccessLevel.NONE)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Version
  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  private int version;

  public boolean active() {
    return status == Status.ACTIVE;
  }

  public enum Status {
    ACTIVE,
    INACTIVE
  }
}
