package helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class DateGenerator {
    public static Date afterDaysFromNow(int days){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date modifiedDate = calendar.getTime();
        return  modifiedDate;
    }
}
