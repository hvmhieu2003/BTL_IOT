package com.iot.iot_BE.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    public enum HttpStatusCodeDescription {
        SUCCESS("Success"),
        CREATED("Created"),
        BAD_REQUEST("Bad request"),
        INTERNAL_SERVER_ERROR("Internal server error"),
        NOT_FOUND("Not found"),
        UNAUTHORIZED("Unauthorized"),
        UNPROCESSABLE_ENTITY("Unprocessable Entity"),
        FORBIDDEN("Forbidden");

        private final String description;

        HttpStatusCodeDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public static Object convertDate(String s) {
        // Biểu thức chính quy để khớp các định dạng
        String dateTimeRegex = "(\\d{2})/(\\d{2})/(\\d{4})(?:\\s(\\d{2})(?::(\\d{2})(?::(\\d{2}))?)?)?";
        Pattern pattern = Pattern.compile(dateTimeRegex);
        Matcher matcher = pattern.matcher(s);

        // Kiểm tra chuỗi có khớp với định dạng không
        if (!matcher.matches()) {
            return false; // Không đúng định dạng
        }

        // Extract các giá trị từ match
        String day = matcher.group(1);
        String month = matcher.group(2);
        String year = matcher.group(3);
        String hour = matcher.group(4) != null ? matcher.group(4) : "00";
        String minute = matcher.group(5) != null ? matcher.group(5) : "00";
        String second = matcher.group(6) != null ? matcher.group(6) : "00";

        // Tạo chuỗi thời gian theo định dạng chuẩn
        String dateString = String.format("%s-%s-%s %s:%s:%s", year, month, day, hour, minute, second);

        // Sử dụng SimpleDateFormat để kiểm tra tính hợp lệ của ngày giờ
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false); // Không cho phép định dạng không hợp lệ

        try {
            Date date = dateFormat.parse(dateString);
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String ans = String.format("%s-%s-%s", year, month, day);

            switch (s.length()) {
                case 16: // 'YYYY-MM-DD HH:mm'.length()
                    return new DateRange(ans + " " + hour + ":" + minute + ":00.000000",
                            ans + " " + hour + ":" + minute + ":59.000000");
                case 13: // 'YYYY-MM-DD HH'.length()
                    return new DateRange(ans + " " + hour + ":00:00.000000",
                            ans + " " + hour + ":59:59.000000");
                case 10: // 'YYYY-MM-DD'.length()
                    return new DateRange(ans + " 00:00:00.000000",
                            ans + " 23:59:59.000000");
                default:
                    return new DateRange(ans + ".000000", ans + ".999999");
            }
        } catch (ParseException e) {
            return false; // Trả về false nếu không hợp lệ
        }
    }

    public static String betweenQuery(DateRange dateRange) {
        return String.format("HistorySensor.created_at >= '%s' AND HistorySensor.created_at <= '%s'",
                dateRange.getStartOfDay(), dateRange.getEndOfDay());
    }

    public static String betweenQuery2(DateRange dateRange) {
        return String.format("HistoryAction.created_at >= '%s' AND HistoryAction.created_at <= '%s'",
                dateRange.getStartOfDay(), dateRange.getEndOfDay());
    }

    public static class DateRange {
        private final String startOfDay;
        private final String endOfDay;

        public DateRange(String startOfDay, String endOfDay) {
            this.startOfDay = startOfDay;
            this.endOfDay = endOfDay;
        }

        public String getStartOfDay() {
            return startOfDay;
        }

        public String getEndOfDay() {
            return endOfDay;
        }
    }
}
