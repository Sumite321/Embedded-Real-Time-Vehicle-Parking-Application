package com.smt.sweettreats.paypark;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ApiRadius extends AppCompatActivity implements Serializable{

    private TextView text, booking_time_f,booking_time_t,input_booking_date;
    private EditText filter,radius;
    private Spinner radiusS;
    Radius getRadius = new Radius();
    private ArrayList<String> filteredPostcode = new ArrayList<>();
    private Button showFiltered;
    public ArrayList<slot> rentalSlots = new ArrayList<>();
    public ArrayList<slot> rentalSlotsUpdated = new ArrayList<>();
    private ArrayList<slot> showFilteredArray = new ArrayList<>();
    private ArrayList<String> availability = new ArrayList<>();
    private String line1,outcode;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_postcode);

        text = (TextView) findViewById(R.id.postcode_search);
        showFiltered = (Button) findViewById(R.id.btn_showbooking);
        filter = (EditText) findViewById(R.id.input_postcode);
        radius = (EditText) findViewById(R.id.input_radius);
        radiusS = (Spinner) findViewById(R.id.spinner_radius);


        List<String> radiusList = new ArrayList<>();


        // populate with the hours and minutes
        // time interval of 10 minutes

        radiusList.add("0");
        radiusList.add("1000");
        radiusList.add("2000");
        radiusList.add("3000");
        radiusList.add("4000");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ApiRadius.this, android.R.layout.simple_spinner_item, radiusList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        radiusS.setAdapter(dataAdapter);


        showFiltered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get datasnapshot at your "users" root node
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref2;
                ref2 = ref1.child("slot");
                // will populate the booking slots
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Result will be holded Here
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            price = Double.valueOf(dsp.child("price").getValue().toString());
                            outcode = dsp.child("postcode").getValue().toString();
                            dsp.child("availability");
                            for(DataSnapshot a: dsp.child("availability").getChildren()){
                                String timeFrom = a.child("from_time").getValue().toString();
                                String timeTill = a.child("till_time").getValue().toString();
                                String date = a.getKey();
                                //System.out.println(date + input_booking_date.getText().toString());
                                // first validation
                                // if the user availability matches the slot availability
                                if(date.equals(input_booking_date.getText().toString())
                                        && checktimings(timeFrom,booking_time_f.getText().toString())
                                        && !checktimings(timeTill,booking_time_t.getText().toString())){
                                    //address = String.valueOf(dsp.getKey());
                                    System.out.println(timeFrom + timeTill);
                                    System.out.println(booking_time_f.getText().toString());
                                    System.out.println(booking_time_t.getText().toString());
                                    System.out.println(outcode);
                                    rentalSlots.add(new slot(String.valueOf(dsp.getKey()), outcode, "London", availability, price, "driveway", 1, false));
                                    System.out.println(rentalSlots);
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                DatabaseReference ref5 = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref6;

                ref6 = ref5.child("booking");
                ref6.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataS) {
                        System.out.println("Working");

                        System.out.println(dataS.child(input_booking_date.getText().toString()).exists());
                        // validation
                        //availability.add("1 Buckingham Road");

                        // if the table has the date
                        if(dataS.child(input_booking_date.getText().toString()).exists()) {

                            // run the loop if the date exists
                            //contains all the addresses for the date requested
                            for (DataSnapshot dsp : dataS.child(input_booking_date.getText().toString()).getChildren()) {
                                System.out.println("Working");
                                System.out.println(dsp.getKey().equals(input_booking_date.getText().toString()));
                                System.out.println(dsp.child(input_booking_date.getText().toString()).getValue() != null);
                                System.out.println(input_booking_date.getText().toString());
                                System.out.println(dsp.getValue().toString());//
                                line1 = dsp.getKey(); // has the address

                                // loop through all the bookingID
                                for (DataSnapshot bookingID : dsp.getChildren()) {
                                    //loop here will go through all the addresses
                                    System.out.println(dsp.getKey()); // has the ID
                                    String from = bookingID.child("from_time").getValue().toString();
                                    String till = bookingID.child("till_time").getValue().toString();
                                    // if checkTimings
                                    if(checktimings(from,booking_time_f.getText().toString())
                                            && !checktimings(till,booking_time_t.getText().toString())){
                                        availability.add(line1);
                                    }
                                }
                            }
                        }
                        System.out.println(availability.toString());

                        new ApiRadius.getPostCodeinRadius().execute(Common.apiRadius(filter.getText().toString(),radius.getText().toString()));

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}

                });



                //System.out.println(rentalSlots);
                //System.out.println(showFilteredArray);

            }

        });

    }


    /* class that does all the Radius work and display to the next page */
    private class getPostCodeinRadius extends AsyncTask<String,Void,String> {
        ProgressDialog pd = new ProgressDialog(ApiRadius.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Looking for slots...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }


        protected void onPostExecute(String result) {


            super.onPostExecute(result);

            if(result == null){
                pd.dismiss();
                System.out.println("No bull found");
                return;
            }

            Gson gson = new Gson();
            Type mType = new TypeToken<Radius>(){}.getType();
            getRadius = gson.fromJson(result,mType);

            Log.d("heyhere2",rentalSlots.toString());
            Log.d("heyhere2",availability.toString());


            // toDelete contains 1 bam
            // rental has 5 bam

            // in here this loop will go through the rental slots and add to rentalSlotsUpdated only the ones availability doesnt have
            for(slot s:rentalSlots){
                Log.d("check","if has run");
                if(!availability.contains(s.getStreetName())){
                    // if contains then dont add
                    Log.d("check2","if has run");
                    Log.d("checkifRentalSlots",rentalSlotsUpdated.toString());

                    rentalSlotsUpdated.add(s);

                }

            }


            // checking if rentalSlots is not empty
            if (!rentalSlotsUpdated.isEmpty()){
                Log.d("check3","rental is not empty");

                for (Location l : getRadius.getResult()) {
                    // add all the outcodes in radius
                    Log.d("checkRadiusOutcode",l.getOutcode());
                    filteredPostcode.add(l.getOutcode());
                    Log.d("checkRFilteredPostcode",filteredPostcode.toString());
                }
                for (slot s : rentalSlotsUpdated) {
                    Log.d("checkSlotOutcode",s.getOutcode());
                    System.out.println(filteredPostcode.contains(s.getOutcode()));
                    System.out.println(availability.toString());
                    if (filteredPostcode.contains(s.getOutcode())) {
                        showFilteredArray.add(s);
                        System.out.println(s.getOutcode());
                    }
                }
            }

            if(!showFilteredArray.isEmpty()) {
                Intent intent = new Intent(ApiRadius.this, bookingListView.class);
                intent.putExtra("filteredPostcode", showFilteredArray);
                Log.d("hey", rentalSlotsUpdated.toString());
                intent.putExtra("from", booking_time_f.getText().toString());
                intent.putExtra("till", booking_time_t.getText().toString());
                intent.putExtra("date", input_booking_date.getText().toString());

                startActivity(intent);
                finish();
                pd.dismiss();
            }else{

                Toast.makeText(ApiRadius.this, "No slots found, change your filters.",
                        Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
            //System.out.println(postcode.getLatitude());
            //System.out.println(postcode.getAddress());
        }
    }
    /* Classes in charge of Time click */

    public void selectFromTime(View view) {
        DialogFragment newFragment = new ApiRadius.SelectFromTimeFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }
    @SuppressLint("ValidFragment")
    public class SelectFromTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hh = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hh, m, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            populateSetFromTime(i,i1);
        }

        public void populateSetFromTime(int hour, int minute) {
            booking_time_f = (TextView)findViewById(R.id.booking_time_f);
            if(minute<10){booking_time_f.setText(hour+":0"+minute);}
            else{booking_time_f.setText(hour+":"+minute);}
        }
    }


    public void selectTillTime(View view) {
        DialogFragment newFragment = new ApiRadius.SelectTillTimeFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }
    @SuppressLint("ValidFragment")
    public class SelectTillTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hh = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hh, m, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            populateSetTillTime(i,i1);
        }

        public void populateSetTillTime(int hour, int minute) {
            booking_time_t = (TextView)findViewById(R.id.booking_time_t);
            if(minute<10){booking_time_t.setText(hour+":0"+minute);}
            else{booking_time_t.setText(hour+":"+minute);}
        }
    }
    /**********************************************/


    public void selectDate(View view) {
        DialogFragment newFragment = new ApiRadius.SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        input_booking_date = (TextView)findViewById(R.id.input_booking_date);
        input_booking_date.setText(String.valueOf(String.format("%d-%d-%d",day,month,year)));
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


    private boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if(date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }
}


