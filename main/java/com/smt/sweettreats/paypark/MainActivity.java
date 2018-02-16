package com.smt.sweettreats.paypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private Button eSendData;
    private DatabaseReference ref;


    private TextView txt;
    private DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        ref = FirebaseDatabase.getInstance().getReference();

        db = new DBConnection(); // initialise the database connection

        eSendData = (Button) findViewById(R.id.suite);

        /*
        eSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase connection  = fb.child("Connection").child("12354");
                connection.setValue("Success CORRECT");
                txt.setText("W");
            }
        });
        */
        eSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, login.class));
            }
        });









    }










}
