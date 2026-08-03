package ru.rodionov.starter.util;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public final class DateUtil {

    public static OffsetDateTime now() {
        return OffsetDateTime.now();
    }

    public static OffsetDateTime truncatedNow() {
        return OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
