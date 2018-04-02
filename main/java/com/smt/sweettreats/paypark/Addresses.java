package com.smt.sweettreats.paypark;

/**
 * Created by admin on 16/02/2018.
 */

public class Addresses {

    private String Line1;
    private String Line2;
    private String Line3;
    private String Line4;
    private String Locality;
    private String City;
    private String County;


    public Addresses(String line1, String line2, String line3, String line4, String locality, String city, String county) {
        Line1 = line1;
        Line2 = line2;
        Line3 = line3;
        Line4 = line4;
        Locality = locality;
        City = city;
        County = county;
    }


    public String getLine1() {
        return Line1;
    }

    public void setLine1(String line1) {
        Line1 = line1;
    }

    public String getLine2() {
        return Line2;
    }

    public void setLine2(String line2) {
        Line2 = line2;
    }

    public String getLine3() {
        return Line3;
    }

    public void setLine3(String line3) {
        Line3 = line3;
    }

    public String getLine4() {
        return Line4;
    }

    public void setLine4(String line4) {
        Line4 = line4;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }
}
