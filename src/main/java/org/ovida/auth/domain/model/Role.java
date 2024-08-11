package org.ovida.auth.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "roleName")
@ToString
@Entity
@Table(name = "role")
public class Role {

  @Id
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
}
