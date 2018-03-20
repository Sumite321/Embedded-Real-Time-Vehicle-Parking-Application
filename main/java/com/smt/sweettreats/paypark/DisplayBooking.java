package com.smt.sweettreats.paypark;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class DisplayBooking extends AppCompatActivity {

    private boolean isRentals = false;
    private TextView idate;
    private Button show;
    public ArrayList<slot> showSlots = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_booking);

        show = (Button) findViewById(R.id.btn_showbook);

        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("booking");

        isRentals = (Boolean) getIntent().getSerializableExtra("isRentals");

        //get the address of the current logged in user (for someone who would like to see their rentals)
        final String address = login.session.getUserDetails().get("address");
        // get their userID (for someone who would like to see their bookings)
        final String userID = login.session.getUserDetails().get("id");
        final String userName = login.session.getUserDetails().get("userid");

        Log.d("check-1_displaybooking",address);
        Log.d("check0_displaybooking",userID);





        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref6 = FirebaseDatabase.getInstance().getReference().child("booking");


                if (isRentals) {
                    ref6.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataS) {
                            // if the table has the date
                            if (dataS.child(idate.getText().toString()).exists()) {
                                // if the table has the address
                                if (dataS.child(idate.getText().toString()).child(address).exists()) {
                                    // loop through all the data
                                    for (DataSnapshot dsp : dataS.child(idate.getText().toString()).child(address).getChildren()) {

                                        // loop through all the bookingID
                                        for (DataSnapshot bookingID : dsp.getChildren()) {
                                            // if the bookingID has the address/userID add to arrayList
                                            showSlots.add(new slot("a", "a", "a", "a", "a", "a"));
                                            // name of the driver
                                            // duration
                                            //total
                                            //from
                                            //till
                                            //address
                                        }
                                    }
                                }
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });

                } else {
                    ref6.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataS) {

                            // if the table has the date
                            if (dataS.child(idate.getText().toString()).exists()) {
                                Log.d("check1_displaybooking","runs");
                                // loop through all the addresses
                                for (DataSnapshot bookingAddress : dataS.child(idate.getText().toString()).getChildren()) {
                                    Log.d("check2_displaybooking",bookingAddress.toString());
                                    Log.d("check3_displaybooking","runs");
                                    // loop through all the id's
                                    for (DataSnapshot dsp : bookingAddress.getChildren()) {
                                        Log.d("check4_displaybooking",dsp.toString());

                                        Log.d("check5_displaybooking","runs");
                                        String address = dsp.getKey();
                                        // loop through all the bookingID
                                        for (DataSnapshot bookingID : dsp.getChildren()) {
                                            Log.d("check6_displaybooking",bookingID.toString());

                                            Log.d("check7_displaybooking","runs");
                                            // if the bookingID has the userID add to arrayList

                                            if (bookingID.getKey().equals("driver_id")) {
                                                Log.d("check8_displaybooking","runs");
                                                Log.d("check12_displaybooking",bookingID.getValue().toString());
                                                if (bookingID.getValue().toString().equals(userID)) {
                                                    Log.d("check13_displaybooking","this has run too");

                                                    showSlots.add(new slot("a", "OWN", "a", "a", "a", "a"));

                                                    Log.d("check18_displaybooking",showSlots.toString());
//duration
                                                    //total
                                                    //from
                                                    //till
                                                    //address


                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Intent intent = new Intent(DisplayBooking.this, bookingListView.class);
                            Log.d("check15_displaybooking",showSlots.toString());

                            intent.putExtra("rentals",isRentals);
                            intent.putExtra("showBooking",showSlots);

                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }




                    });

                }



            }
    });
}


    public void selectDate(View view) {
        DialogFragment newFragment = new DisplayBooking.SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        idate = (TextView)findViewById(R.id.input_date);
        idate.setText(String.valueOf(String.format("%d-%d-%d",day,month,year)));
    }
    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
    }




}



