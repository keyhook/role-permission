package org.ovida.auth.app.search;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@ToString(exclude = "records")
public class SearchResult {

  private List<?> records;

  private int page;

  private int pageSize;

  private long total;
}