package com.smt.sweettreats.paypark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 22/02/2018.
 */

public class Radius implements Serializable{


    private int status;
    private List<Location> result = new ArrayList<>();


    public Radius(){}



    public Radius(int status, List<Location> result) {
        this.status = status;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public List<Location> getResult() {
        return result;
    }
}
