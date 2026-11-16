package ru.rodionov.starter.util;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public interface Constants {
    String BEAN_FACTORY_GENERIC_RETURN_TYPE = "genericReturnType";

    String REDIS_KEY_WILDCARD = "*";
    String REDIS_KEY_SEPARATOR = ":";
    Long REDIS_MAX_SCAN_COUNT = 500000L;
    Long REDIS_PAGE_SCAN_COUNT = 5000L;
    Duration DEFAULT_REDIS_TTL = Duration.ofHours(12);
    Integer MAX_MESSAGE_BUFFER_SIZE = 65536;

    String X_ACCESS_TOKEN = "X-ACCESS-TOKEN";
    String X_API_TOKEN = "X-API-TOKEN";
    String X_EMAIL = "X-EMAIL";
    String HTTP_SESSION_ID = "httpSessionId";
    String HTTP_SESSION = "httpSession";
    String DEFAULT_SORT_FIELD = "id";
    String REASON_PATTERN = "{reason}";
    String OBJECT_PATTERN = "{object}";
    String FIELD_PATTERN = "{field}";

    List<String> ALLOWED_IMAGE_MIME_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif"
    );
}
