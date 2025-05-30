package com.sprint.mission.discodeit.dto.response;

import java.util.List;

@FunctionalInterface
public interface CursorExtractor<T> {

  Object nextCursor(List<T> content);
}
