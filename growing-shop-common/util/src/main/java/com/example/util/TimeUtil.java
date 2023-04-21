package com.example.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtil {
    private TimeUtil() {
    }

    public static LocalDateTime convertDateToLocalDateTime(Date source) {
        return Instant.ofEpochMilli(source.getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
