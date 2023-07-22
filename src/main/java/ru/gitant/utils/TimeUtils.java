package ru.gitant.utils;

import com.binance.client.model.enums.CandlestickInterval;

import java.time.*;

public class TimeUtils {
    public static final long MINUTES_TO_MILLISECONDS_CONVERTER = 60000;
    public static final long HOURS_TO_MILLISECONDS_CONVERTER = 60 * MINUTES_TO_MILLISECONDS_CONVERTER;
    public static final long DAYS_TO_MILLISECONDS_CONVERTER = 24 * HOURS_TO_MILLISECONDS_CONVERTER;
    public static final long MONTHS_TO_MILLISECONDS_CONVERTER = 30 * DAYS_TO_MILLISECONDS_CONVERTER;
    public static final long WEEKS_TO_MILLISECONDS_CONVERTER = 7 * DAYS_TO_MILLISECONDS_CONVERTER;

    public static Long candleStickIntervalToMilliseconds(CandlestickInterval interval) {
        String intervalCode = interval.toString();
        int value = Integer.parseInt(intervalCode.substring(0, intervalCode.length() - 1));
        char typeOfTime = intervalCode.charAt(intervalCode.length() - 1);
        return switch (typeOfTime) {
            case 'm' -> (long) value * MINUTES_TO_MILLISECONDS_CONVERTER;
            case 'h' -> (long) value * HOURS_TO_MILLISECONDS_CONVERTER;
            case 'd' -> (long) value * DAYS_TO_MILLISECONDS_CONVERTER;
            case 'w' -> (long) value * WEEKS_TO_MILLISECONDS_CONVERTER;
            case 'M' -> (long) value * MONTHS_TO_MILLISECONDS_CONVERTER;
            default -> -1L;
        };
    }

    public static ZonedDateTime getZonedDateTime(Long timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static String getTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalTime localTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
        return String.format("%02d:%02d:%02d.%d",
                localTime.getHour(),
                localTime.getMinute(),
                localTime.getSecond(),
                (timestamp % 1000));
    }
}
