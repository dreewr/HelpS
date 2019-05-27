package com.ebmacs.helpapp;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Usman on 12/5/2017.
 */

public class ConvertDate {

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(String d, Context ctx) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);

        date = calendar.getTime();

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = null;
        try {
            if (dim == 0) {
                timeAgo = ctx.getResources().getString(R.string.date_util_term_less) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_minute);
            } else if (dim == 1) {
                return "1" + ctx.getResources().getString(R.string.date_util_unit_minute);
            } else if (dim >= 2 && dim <= 44) {
                timeAgo = dim + ctx.getResources().getString(R.string.date_util_unit_minutes);
            } else if (dim >= 45 && dim <= 89) {
                timeAgo = "1" + ctx.getResources().getString(R.string.date_util_unit_hour);
            } else if (dim >= 90 && dim <= 1439) {
                timeAgo = (Math.round(dim / 60)) + ctx.getResources().getString(R.string.date_util_unit_hours);
            } else if (dim >= 1440 && dim <= 2519) {
                timeAgo = "1" + ctx.getResources().getString(R.string.date_util_unit_day);
            } else if (dim >= 2520 && dim <= 43199) {
                timeAgo = (Math.round(dim / 1440)) + ctx.getResources().getString(R.string.date_util_unit_days);
            } else if (dim >= 43200 && dim <= 86399) {
                timeAgo = ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_month);
            } else if (dim >= 86400 && dim <= 525599) {
                timeAgo = (Math.round(dim / 43200)) + ctx.getResources().getString(R.string.date_util_unit_months);
            } else if (dim >= 525600 && dim <= 655199) {
                timeAgo = "1" + ctx.getResources().getString(R.string.date_util_unit_year);
            } else if (dim >= 655200 && dim <= 914399) {
                timeAgo = "1" + ctx.getResources().getString(R.string.date_util_unit_year);
            } else if (dim >= 914400 && dim <= 1051199) {
                timeAgo = "2" + ctx.getResources().getString(R.string.date_util_unit_years);
            } else {
                timeAgo = (Math.round(dim / 525600)) + " " + ctx.getResources().getString(R.string.date_util_unit_years);
            }

        return timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    public static String chatDate(long mili){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mili);
        Date date = c.getTime();


        long timeInMili = date.getTime();

        boolean today = DateUtils.isToday(timeInMili);
        if(today){
            return "Today";
        }
        else{
            boolean yes = isYesterday(date);
            if(yes){
                return "Yesterday";
            }
        }


        String final_date = simpleDateFormat.format(date);
        return final_date;

    }

    public static String inboxDate(String d){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = sdf.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        long timeInMili = date.getTime();
//
//        boolean today = DateUtils.isToday(timeInMili);
//        if(today){
//            return "Today at " + time.format(date);
//        }
//        else{
//            boolean yes = isYesterday(date);
//            if(yes){
//                return "Yesterday at " + time.format(date);
//            }
//        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);

        date = calendar.getTime();

        String final_date = simpleDateFormat.format(date);
        return final_date;

    }

    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }


    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }

}
