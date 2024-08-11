package org.ovida.auth.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "privilegeName")
@ToString
@Entity
@Table(name = "privilege")
public class Privilege {

  @Id
  private String privilegeName;

  @Setter(AccessLevel.NONE)
  @CreatedDate
  private LocalDateTime createdAt;

  @Setter(AccessLevel.NONE)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Version
  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  private int version;
}
