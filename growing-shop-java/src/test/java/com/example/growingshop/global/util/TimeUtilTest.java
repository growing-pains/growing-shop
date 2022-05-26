package com.example.growingshop.global.util;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


class TimeUtilTest {

    @Test
    void Date_To_LocalDateTime_으로_변환_테스트() {
        Date now = new Date();

        LocalDateTime convertedResult = TimeUtil.convertDateToLocalDateTime(now);

        assertThat(now.compareTo(Timestamp.valueOf(convertedResult))).isEqualTo(0);
    }
}