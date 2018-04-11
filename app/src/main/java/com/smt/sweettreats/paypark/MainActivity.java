package com.smt.sweettreats.paypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Serializable{

    private static final String TAG = "";
    private Button toRegister, toBook,toLogin;
    private DatabaseReference ref;
    public static SessionManager session;

    private TextView txt;
    private DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        session = new SessionManager(getApplicationContext());
        db = new DBConnection(); // initialise the database connection

        toRegister = (Button) findViewById(R.id.suite);
        toBook = (Button) findViewById(R.id.suite2);
        toLogin = (Button) findViewById(R.id.loginNow);





        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, register.class));
            }
        });


        toBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ApiRadius.class);
                startActivity(intent);
                finish();
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                finish();
            }
        });


    }

}
