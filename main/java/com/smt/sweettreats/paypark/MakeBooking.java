package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    private TextView address,price,duration,from, till, date,total,nameT;
    private double priceTotal;
    private int time, key;
    private Button checkout;
    private String name;
    private List<String> slots = new ArrayList<>();
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);



        address = (TextView) findViewById(R.id.raddress);
        price = (TextView) findViewById(R.id.rprice);
        duration = (TextView) findViewById(R.id.rduration);
        from = (TextView) findViewById(R.id.rtextFrom);
        till = (TextView) findViewById(R.id.rtextTill);
        date = (TextView) findViewById(R.id.rtextDate);
        total = (TextView) findViewById(R.id.rtextTotal);
        checkout = (Button) findViewById(R.id.btn_checkout);
        nameT = (TextView) findViewById(R.id.rname);



        slots = (ArrayList<String>) getIntent().getSerializableExtra("slotD");
        key = (Integer) getIntent().getSerializableExtra("userID");
        name = (String)getIntent().getSerializableExtra("userName");
                System.out.println(slots.get(0));


        nameT.setText(name);
        address.setText(slots.get(0));
        price.setText(slots.get(1));
        duration.setText(slots.get(2));
        from.setText(slots.get(3));
        till.setText(slots.get(4));
        date.setText(slots.get(5));
        total.setText(slots.get(6));



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
                                .child(String.valueOf(key)).child("driver_name");
                        usersName.setValue(name);

                        // id of the who booked
                        DatabaseReference usersID = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key)).child(name);
                        usersID.setValue(name);

                        // duration
                        DatabaseReference bookingDuration = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key)).child(slots.get(2));
                        bookingDuration.setValue(name);

                        // from time
                        DatabaseReference bookingFrom = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key)).child(slots.get(3));
                        bookingFrom.setValue(name);

                        // till time
                        DatabaseReference bookingTill = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key)).child(slots.get(4));
                        bookingTill.setValue(name);

                        // till time
                        DatabaseReference bookingTotal = database.getReference("booking")
                                .child(String.valueOf(slots.get(5)))
                                .child(slots.get(0))
                                .child(String.valueOf(key)).child(slots.get(4));
                        bookingTotal.setValue(slots.get(6));



                        Toast.makeText(MakeBooking.this, String.format("Slot registered"),
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MakeBooking.this,success.class));
                        finish();
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
