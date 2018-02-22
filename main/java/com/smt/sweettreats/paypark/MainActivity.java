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
    private Button toRegister, toBook;
    private DatabaseReference ref;
    public ArrayList<slot> rentalSlots = new ArrayList<>();


    private TextView txt;
    private DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);


        db = new DBConnection(); // initialise the database connection

        toRegister = (Button) findViewById(R.id.suite);
        toBook = (Button) findViewById(R.id.suite2);


        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, register.class));
            }
        });


        //Get datasnapshot at your "users" root node
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2, ref3, ref4;
        ref2 = ref1.child("slot");


        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    double price =  Double.valueOf(dsp.child("price").getValue().toString());
                    String time = dsp.child("availability").getValue().toString();
                    rentalSlots.add(new slot(String.valueOf(dsp.getKey()), "London", "Available to book",price, "slot_image_1" , 1, false));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        toBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Test");
                System.out.println(rentalSlots);
                Intent intent = new Intent(MainActivity.this, bookingListView.class);
                intent.putExtra("mylist", rentalSlots);
                startActivity(intent);
                finish();
            }
        });

    }
}
