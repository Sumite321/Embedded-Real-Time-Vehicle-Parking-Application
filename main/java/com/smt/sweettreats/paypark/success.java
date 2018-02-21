package com.smt.sweettreats.paypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class success extends AppCompatActivity {


    private Button login;
    private TextView text;
    //private ArrayList<slot> rentalSlots = new ArrayList<>();
    private bookingListView bookingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);


        login = (Button) findViewById(R.id.btn_loginow);
        text = (TextView) findViewById(R.id.registered);

        //Get datasnapshot at your "users" root node
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2, ref3, ref4;
        ref2 = ref1.child("slot");

        bookingList= new bookingListView();

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> Userlist = new ArrayList<String>();
                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(String.valueOf(dsp.getKey() +
                            dsp.child("price").getValue() +
                            dsp.child("availability").getValue())); //add result into array list

                    String address = dsp.getKey();
                    bookingList.rentalSlots.add(new slot(10,"jjk", "asd", "asd", "as", 4300.0, "none",1,2,3,true));
                    System.out.println("Test");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(success.this,bookingListView.class ));

                finish();
            }
        });


        for(slot s:bookingList.rentalSlots){
            System.out.print("This");
            System.out.print(s.getStreetName());
        }

    }


}