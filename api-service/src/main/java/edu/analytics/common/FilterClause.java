package edu.analytics.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterClause {
  private String field;
  private Operator operation;
  private String rawValue;
}
