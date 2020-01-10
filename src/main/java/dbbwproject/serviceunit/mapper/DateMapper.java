package dbbwproject.serviceunit.mapper;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateMapper {
    private static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static Date toDateObj(String dateStr) {
        DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException("can not convert dateStr: " + dateStr + " to Date.");
        }
    }

    public static String toDateStr(Date date) {
        DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        try {
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("can not convert dateStr: " + date + " to DateString.");
        }
    }

}
