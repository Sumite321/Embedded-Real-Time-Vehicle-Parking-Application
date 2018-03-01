package com.smt.sweettreats.paypark;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApiRadius extends AppCompatActivity implements Serializable{

    private TextView text, booking_time_f,booking_time_t;
    private EditText filter,radius;
    Radius getRadius = new Radius();
    private ArrayList<String> filteredPostcode = new ArrayList<>();
    private Button showFiltered;
    public ArrayList<slot> rentalSlots = new ArrayList<>();
    private ArrayList<slot> showFilteredArray = new ArrayList<>();
    private ArrayList<String> availability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_postcode);

        text = (TextView) findViewById(R.id.postcode_search);
        showFiltered = (Button) findViewById(R.id.btn_showbooking);
        filter = (EditText) findViewById(R.id.input_postcode);
        radius = (EditText) findViewById(R.id.input_radius);

        showFilteredArray.clear();
        //Get datasnapshot at your "users" root node
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2, ref3;
        ref2 = ref1.child("slot");
        ref3 = ref2.child("slot").child("availability");

        // will populate the booking slots
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

        // Result will be holded Here
            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                double price = Double.valueOf(dsp.child("price").getValue().toString());
                String outcode = dsp.child("postcode").getValue().toString();
                dsp.child("availability");
                for(DataSnapshot a: dsp.child("availability").getChildren()){
                    String timeFrom = a.child("from_time").getValue().toString();
                    String timeTill = a.child("till_time").getValue().toString();
                    String date = a.getKey();
                    availability = new ArrayList<>();
                    availability.add(date);
                    availability.add(timeFrom);
                    availability.add(timeTill);
                    rentalSlots.add(new slot(String.valueOf(dsp.getKey()), outcode, "London", availability, price, "driveway", 1, false));
                }
            }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        showFiltered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ApiRadius.getPostCodeinRadius().execute(Common.apiRadius(filter.getText().toString(),radius.getText().toString()));
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
            pd.setTitle("Please wait...");
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

            for (Location l: getRadius.getResult()){
                filteredPostcode.add(l.getOutcode());
                //System.out.println(l.getDistance());
            }
            for(slot s:rentalSlots){
                System.out.println(filteredPostcode.contains(s.getOutcode()));
                if(filteredPostcode.contains(s.getOutcode())){showFilteredArray.add(s);System.out.println(s.getOutcode());
                }
            }

            Intent intent = new Intent(ApiRadius.this, bookingListView.class);
            intent.putExtra("filteredPostcode", showFilteredArray);
            intent.putExtra("fromTime",booking_time_f.getText().toString());
            intent.putExtra("tillTime", booking_time_t.getText().toString());
            startActivity(intent);
            finish();
            pd.dismiss();

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




}


