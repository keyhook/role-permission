package org.ovida.auth.app.search;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SearchQuery {

  @Min(0)
  @Max(50)
  private int page = 0;

  @Min(5)
  @Max(100)
  private int pageSize = 10;
}
