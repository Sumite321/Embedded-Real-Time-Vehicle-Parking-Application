package com.smt.sweettreats.paypark;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 16/02/2018.
 */

public class Common {
        private static String API_KEY = "QWFfCcjHoEqgQljq5LSTPw12936";
        private static String API_LINK = "https://api.getaddress.io/find/";
        private static String API_LINK2 = "https://api.postcodes.io/outcodes/"; //"https://api.postcodes.io/outcodes/HA0/nearest?radius=2000"


        @NonNull
        public static String apiRequest(String postC){
            StringBuilder sb = new StringBuilder(API_LINK);
            sb.append(String.format("%s?api-key=%s",postC,API_KEY));
            return sb.toString();
        }

        public static String apiRadius(String post, String radius){

            StringBuilder sb = new StringBuilder(API_LINK2);
            sb.append(String.format("%s/nearest?radius=%s",post,radius));
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
