package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.List;

public class MakeBooking extends AppCompatActivity {


    private TextView address,price,duration,from, till, date,total,nameT,availability;
    private double priceTotal;
    private int time, userKey;
    private Button checkout;
    private String name, addressData;
    private SessionManager session;

    private List<String> slots = new ArrayList<>();
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);


        session = MainActivity.session;
        address = (TextView) findViewById(R.id.raddress);
        price = (TextView) findViewById(R.id.rprice);
        duration = (TextView) findViewById(R.id.rduration);
        from = (TextView) findViewById(R.id.rtextFrom);
        till = (TextView) findViewById(R.id.rtextTill);
        date = (TextView) findViewById(R.id.rtextDate);
        total = (TextView) findViewById(R.id.rtextTotal);
        checkout = (Button) findViewById(R.id.btn_checkout);
        nameT = (TextView) findViewById(R.id.rname);
        availability = (TextView) findViewById(R.id.available);

        if(session.isLoggedIn()){
            slots = (ArrayList<String>) getIntent().getSerializableExtra("slotD");
            userKey = Integer.valueOf( session.getUserDetails().get("id"));
            name = session.getUserDetails().get("userid");


        }
        else {
            slots = (ArrayList<String>) getIntent().getSerializableExtra("slotD");
            userKey = (Integer) getIntent().getSerializableExtra("userID");
            name = (String) getIntent().getSerializableExtra("userName");
            System.out.println(slots.get(0));
        }

            addressData = slots.get(0);
        nameT.setText(name);
        address.setText(slots.get(0));
        price.setText(slots.get(1));
        duration.setText(slots.get(2));
        from.setText(slots.get(3));
        till.setText(slots.get(4));
        date.setText(slots.get(5));
        total.setText(slots.get(6));

        // check if sensor exists for the slot
            // if the sensor exists, get the real time data from it
            // allow checkout button only and only if slot available
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
               /* ******** Registration to database  ******** */

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(MakeBooking.this);
                pd.setTitle("Validating...");
                pd.show();


                // get the current ID registered
                final DatabaseReference mDatabasePlayers = FirebaseDatabase.getInstance().getReference().child("booking");
                mDatabasePlayers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){

                        int key = 0; // contains the current key


                        //if date exists
                        if(dataSnapshot.child(slots.get(5)).exists()){
                            // if address exists
                            if(dataSnapshot.child(slots.get(5)).child(slots.get(0)).exists()){
                                if(dataSnapshot.child(slots.get(5)).child(slots.get(0)).hasChildren()){
                                    for(DataSnapshot s: dataSnapshot.child(slots.get(5)).child(slots.get(0)).getChildren()){
                                        key = Integer.parseInt(s.getKey());
                                    }
                                    key = key +1 ;
                                }
                            }
                        }


                        //String key1 = String.valueOf(key);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        // name of who booked
                        DatabaseReference usersName = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key))
                                .child("driver_name");
                        usersName.setValue(name);

                        // id of the who booked
                        DatabaseReference usersID = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key))
                                .child("driver_id");
                        usersID.setValue(String.valueOf(userKey));

                        // duration
                        DatabaseReference bookingDuration = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key))
                                .child("duration");
                        bookingDuration.setValue(slots.get(2));


                        // from time
                        DatabaseReference bookingFrom = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key))
                                .child("from_time");
                        bookingFrom.setValue(slots.get(3));

                        // till time
                        DatabaseReference bookingTill = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key))
                                .child("till_time");
                        bookingTill.setValue(slots.get(4));

                        // price
                        DatabaseReference bookingTotal = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key))
                                .child("price");
                        bookingTotal.setValue(slots.get(6));

                        // Status
                        DatabaseReference bookingStatus = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key))
                                .child("status");
                        bookingStatus.setValue("confirmed");

                        Toast.makeText(MakeBooking.this, String.format("Slot registered"),
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MakeBooking.this,success.class));
                        finish();
                        pd.dismiss();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException(); // don't swallow errors
                    }
                });
            }
        });


    }
}
