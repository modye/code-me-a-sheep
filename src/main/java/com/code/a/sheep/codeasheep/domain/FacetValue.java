package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacetValue {
  // TODO could be an object instead of a String, but keep it easy for now
  private final String key;
  private final int count;
}
