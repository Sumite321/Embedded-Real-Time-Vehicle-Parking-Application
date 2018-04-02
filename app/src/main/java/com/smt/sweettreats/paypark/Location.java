package com.smt.sweettreats.paypark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 16/02/2018.
 */

public class Location {

    private String latitude;
    private String longitude;
    private String outcode;
    private String distance;
    private List<String> addresses = new ArrayList<>();
    private List<Addresses> compiledAddresses = new ArrayList<>();


    public Location(){}


    public Location(String latitude, String longitude, String outcode, String distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.outcode = outcode;
        this.distance = distance;
    }

    public Location(String lat, String lon, List<String> addresses){
        this.addresses = addresses;
        this.latitude = lat;
        this.longitude = lon;

    }

    public List<String> getAddress() {
        return addresses;
    }

    public List<Addresses> getCompiledAddresses() {
        return compiledAddresses;
    }

    public String getLongitude() {

        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {

        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public String getOutcode() {
        return outcode;
    }

    public String getDistance() {
        return distance;
    }
}
