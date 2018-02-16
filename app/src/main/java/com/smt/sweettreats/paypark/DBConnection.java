package com.smt.sweettreats.paypark;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by admin on 15/02/2018.
    This class will handle the connection to the DB + RW
 */



public class DBConnection {


    final Firebase fb;
    final private String URL = "https://paypark-88594.firebaseio.com/";

// ...
    DBConnection(){
        fb = new Firebase(URL);
    }





}





