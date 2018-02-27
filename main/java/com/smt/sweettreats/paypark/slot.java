package com.smt.sweettreats.paypark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 21/02/2018.
 */

//Base class to hold information about our property
public class slot implements Serializable{

    //property basics
    //private int streetNumber;
    private String streetName;
    private String suburb;
    private String outcode;
    private List<String> description = new ArrayList<>();
    private String image;
    private Double price;
    //private int bedrooms;
    //private int bathrooms;
    private int carspots;
    private Boolean featured;

    //constructor
    public slot( String streetName, String outcode, String suburb,
            List<String> description, Double price, String image, int carspots, Boolean featured){

        //this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.suburb = suburb;
        this.outcode = outcode;
        this.description = description;
        this.price = price;
        this.image = image;
        //this.bedrooms = bedrooms;
        //this.bathrooms = bathrooms;
        this.carspots = carspots;
        this.featured = featured;
    }

    //getters
    //public int getStreetNumber() { return streetNumber; }
    public String getStreetName() {return streetName; }
    public String getSuburb() {return suburb; }
    public String getOutcode() {return outcode; }

    public List<String> getDescription() {
        return description;
    }

    public Double getPrice() {return price; }
    public String getImage() { return image; }
   // public int getBedrooms(){ return bedrooms; }
   // public int getBathrooms(){ return bathrooms; }
    public int getCarspots(){ return carspots; }
    public Boolean getFeatured(){return featured; }
}
