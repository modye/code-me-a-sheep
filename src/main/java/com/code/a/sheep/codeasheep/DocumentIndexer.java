package com.code.a.sheep.codeasheep;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DocumentIndexer {
  void indexDocuments(@NotNull List<Map<String, Object>> documents) throws IOException;
}
