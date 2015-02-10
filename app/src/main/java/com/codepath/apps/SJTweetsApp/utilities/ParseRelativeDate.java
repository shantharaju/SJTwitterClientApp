package com.codepath.apps.SJTweetsApp.utilities;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;

/**
 * Created by sjayanna on 2/8/15.
 */
public class ParseRelativeDate {

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJSONDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJSONDate).getTime();
            long flags = DateUtils.SECOND_IN_MILLIS |
                         DateUtils.FORMAT_ABBREV_ALL |
                         DateUtils.FORMAT_ABBREV_RELATIVE |
                         DateUtils.FORMAT_ABBREV_TIME;

            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), flags).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
