package com.smt.sweettreats.paypark;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 16/02/2018.
 */

public class Common {
        public static String API_KEY = "T0WVNrizz06OmAvknC_1yA12330";
        public static String API_LINK = "https://api.getaddress.io/find/";

        @NonNull
        public static String apiRequest(String postC){
            StringBuilder sb = new StringBuilder(API_LINK);
            sb.append(String.format("%s?api-key=%s",postC,API_KEY));
            return sb.toString();
        }

/*
        public static String unixTimeStampToDateTime (double unixTimeStamp){

            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            date.setTime((long)unixTimeStamp*1000);
            return dateFormat.format(date);
        }

        public static String getDateNow(){
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            Date date = new Date();
            return dateFormat.format(date);
        }
*/
    }
