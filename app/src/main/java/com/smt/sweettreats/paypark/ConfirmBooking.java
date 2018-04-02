package com.smt.sweettreats.paypark;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfirmBooking extends AppCompatActivity implements Serializable{


    private TextView address,price,duration,from, till, date,total,availability;
    private double priceTotal;
    private int time;
    private Button checkout;
    private  ArrayList<String> slotDetails = new ArrayList<>();
    private  String addressData, fromTime, tillTime, dateV;
    private  double priceData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);


        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        duration = (TextView) findViewById(R.id.duration);
        from = (TextView) findViewById(R.id.textFrom);
        till = (TextView) findViewById(R.id.textTill);
        date = (TextView) findViewById(R.id.textDate);
        total = (TextView) findViewById(R.id.textTotal);
        checkout = (Button) findViewById(R.id.btn_checkout);
        availability = (TextView) findViewById(R.id.available);


        addressData = getIntent().getExtras().getString("address");
        //
        priceData = getIntent().getExtras().getDouble("price");
        String outcodeData = getIntent().getExtras().getString("outcode");
        fromTime = getIntent().getExtras().getString("from");
        tillTime = getIntent().getExtras().getString("till");
        dateV = getIntent().getExtras().getString("date");


        try {
            time = differenceTime(fromTime, tillTime);
            priceTotal = (priceData / 60) * time;
        } catch (ParseException e) {
            // some error
        }

        address.setText(addressData);
        price.setText(String.format("£ %s0", String.valueOf(priceData)));
        duration.setText(String.format("%d minutes", time));
        from.setText(fromTime);
        till.setText(tillTime);
        date.setText(dateV);
        total.setText(String.format("£ %.2f0", priceTotal));


        DatabaseReference ref6 = FirebaseDatabase.getInstance().getReference();

        ref6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataS) {
                Log.d("text1","runs");
                // if the table has the date
                Log.d("testString",dataS.child("slot").child(addressData).child("sensor").toString());
                if(dataS.child("slot").child(addressData).child("sensor").exists()) {// if the sensor exists
                    //Log.d("text","runs");
                    //dataS.child("slots").child(addressData).child("Sensor").getValue(); // get the value and information
                    dataS.child("Sensor").child(dataS.child("slot").child(addressData).child("sensor").getValue().toString());
                    //Log.d("text2",dataS.child("Sensor").child(dataS.child("slot").child(addressData).child("sensor").getValue().toString()).toString());
                    //from.setText(dataS.child("Sensor").child(dataS.child("slot").child(addressData).child("sensor").getValue().toString()).child("available").getValue().toString());
                    if(dataS.child("Sensor").child(dataS.child("slot").child(addressData).child("sensor").getValue().toString()).child("available").getValue().toString().equals("available")){
                        availability.setBackgroundColor(Color.GREEN);
                        checkout.setEnabled(true);
                        checkout.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                    }else{
                        availability.setBackgroundColor(Color.RED);
                        checkout.setEnabled(false);
                        checkout.setBackgroundColor(Color.GRAY);
                        //Toast.makeText()
                    }
                }
            }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
        });


        // check if sensor exists for the slot
            // if the sensor exists, get the real time data from it
            // allow checkout button only and only if slot available
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slotDetails.add(addressData);
                slotDetails.add(String.valueOf(priceData));
                slotDetails.add(String.valueOf(time));
                slotDetails.add(fromTime);
                slotDetails.add(tillTime);
                slotDetails.add(dateV);
                slotDetails.add(String.format("£ %.2f0", priceTotal));


                Intent intent = new Intent(ConfirmBooking.this, AboutYou.class);
                intent.putExtra("slotD", slotDetails);

                startActivity(intent);

            }
        });



    }

        public int differenceTime(String from, String till) throws ParseException{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = simpleDateFormat.parse(from);
        Date endDate = simpleDateFormat.parse(till);

        long difference = endDate.getTime() - startDate.getTime();
        if(difference<0)
        {
            Date dateMax = simpleDateFormat.parse("24:00");
            Date dateMin = simpleDateFormat.parse("00:00");
            difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
        }
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        return 60*hours+min;
    }


}
