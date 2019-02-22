package com.code.a.sheep.codeasheep;

import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.TopDocs;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public interface DocumentSearcher {
  //TODO generify this function (exceptions and returned value
  TopDocs searchDocuments(@NotNull String query) throws QueryNodeException, IOException;
}
