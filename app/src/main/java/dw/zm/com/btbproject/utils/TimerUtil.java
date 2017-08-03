package dw.zm.com.btbproject.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aaron on 2017/7/4.
 */

public class TimerUtil {

    public static final String getTime(long secondTime) {
        String result = "";

        long mSecondTime = secondTime * 1000;
        Date date = new Date();
        date.setTime(mSecondTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = sdf.format(date);

        return result;
    }

    public static final String getDayTime(long secondTime) {
        String result = "";

        long mSecondTime = secondTime * 1000;
        Date date = new Date();
        date.setTime(mSecondTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        result = sdf.format(date);

        return result;
    }
}
