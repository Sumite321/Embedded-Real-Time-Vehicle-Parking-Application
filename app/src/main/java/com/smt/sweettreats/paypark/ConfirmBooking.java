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


    private TextView address,price,duration,from, till, date,total,availability,title3;
    private double priceTotal;
    private int time;
    private Button checkout,edit,cancel;
    private SessionManager session;

    private  ArrayList<String> slotDetails = new ArrayList<>();
    private  String addressData, fromTime, tillTime, dateV,bookID;
    private  double priceData;
    private boolean editOrCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        session = MainActivity.session;
        address = (TextView) findViewById(R.id.address);
        title3 = (TextView) findViewById(R.id.title3);
        price = (TextView) findViewById(R.id.price);
        duration = (TextView) findViewById(R.id.duration);
        from = (TextView) findViewById(R.id.textFrom);
        till = (TextView) findViewById(R.id.textTill);
        date = (TextView) findViewById(R.id.textDate);
        total = (TextView) findViewById(R.id.textTotal);
        checkout = (Button) findViewById(R.id.btn_checkout);
        availability = (TextView) findViewById(R.id.available);
        edit = (Button) findViewById(R.id.btn_edit);
        cancel = (Button) findViewById(R.id.btn_cancel);

        bookID = getIntent().getExtras().getString("bookID");

        addressData = getIntent().getExtras().getString("address");
        //
        priceData = getIntent().getExtras().getDouble("price");
        String outcodeData = getIntent().getExtras().getString("outcode");
        fromTime = getIntent().getExtras().getString("from");
        tillTime = getIntent().getExtras().getString("till");
        dateV = getIntent().getExtras().getString("date");


        editOrCancel = (Boolean) getIntent().getSerializableExtra("editOrCancel");

        try {
            time = differenceTime(fromTime, tillTime);
            priceTotal = (priceData / 60) * time;
        } catch (ParseException e) {
            // some error
        }

        address.setText(addressData);
        // no need of price field if editOrCancel
        price.setText(String.format("£ %s0", String.valueOf(priceData)));
        if(editOrCancel){price.setVisibility(View.GONE);title3.setVisibility(View.GONE);}
        duration.setText(String.format("%d minutes", time));
        from.setText(fromTime);
        till.setText(tillTime);
        date.setText(dateV);
        total.setText(String.format("£ %.2f0", priceTotal));
        // set the total to the booking total
        if(editOrCancel){total.setText(String.valueOf(priceData));
        checkout.setVisibility(View.GONE);
        }else{
            edit.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);

        }

        DatabaseReference ref6 = FirebaseDatabase.getInstance().getReference();
        // sensor data
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
                        if(!editOrCancel) {
                            checkout.setEnabled(true);
                            checkout.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                        }
                    }else{
                        if(!editOrCancel) {
                            availability.setBackgroundColor(Color.RED);
                            checkout.setEnabled(false);
                            checkout.setBackgroundColor(Color.GRAY);
                        }
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
                slotDetails.add(String.valueOf(priceTotal));

                if(session.isLoggedIn()){
                    //if logged in
                    Intent intent = new Intent(ConfirmBooking.this, MakeBooking.class);
                    intent.putExtra("slotD", slotDetails);
                    startActivity(intent);

                }else {
                    Intent intent = new Intent(ConfirmBooking.this, AboutYou.class);
                    intent.putExtra("slotD", slotDetails);
                    startActivity(intent);
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //firebase.child(id).removeValue();


                DatabaseReference ref7 = FirebaseDatabase.getInstance().getReference().child("booking").child(dateV).child(addressData)
                        .child(bookID);
                ref7.removeValue();
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
