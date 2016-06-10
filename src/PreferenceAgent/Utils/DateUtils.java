package PreferenceAgent.Utils;

import PreferenceAgent.PreferenceAgent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by thomas on 10-6-16.
 */
public class DateUtils {

    public enum Day {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
    }

    public enum DayPart {
        NIGHT,
        MORNING,
        AFTERNOON,
        EVENING,
    }

    public static Day getCurrentDay(){
        return Day.values()[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static DayPart getCurrentDayPart(){
        Date date = Calendar.getInstance().getTime();
        DateFormat.getDateInstance().format(date);

        SimpleDateFormat format = new SimpleDateFormat("HH");
        int currentHour = Integer.parseInt(format.format(date));

        if (currentHour >= 0 && currentHour < 6) { // night
            return DayPart.values()[0];
        } else if (currentHour >= 6 && currentHour < 12) { // morning
            return DayPart.values()[1];
        } else if (currentHour >= 12 && currentHour < 18) { // afternoon
            return DayPart.values()[2];
        } else if (currentHour >= 18 && currentHour < 24) { // evening
            return DayPart.values()[3];
        }
        return null;
    }
}
