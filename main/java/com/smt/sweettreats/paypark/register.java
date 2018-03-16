package com.smt.sweettreats.paypark;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class register extends AppCompatActivity {


    String stream = null;
    Location postcode = new Location();
    private EditText postData;
    Spinner dropdown;
    private Button search,next;
    public Spinner spinner,spin_prices;
    private TextView loginLink, myName, myEmail, input_date,input_time_from,input_time_till;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /* ******** Components ******** */
        loginLink = (TextView) findViewById(R.id.link_login);
        search = (Button) findViewById(R.id.btn_search);
        postData = (EditText) findViewById(R.id.input_address);
        next = (Button) findViewById(R.id.btn_nxt);
        spinner = (Spinner) findViewById(R.id.spinner1);
        myName = (TextView)  findViewById(R.id.input_name);
        myEmail = (TextView) findViewById(R.id.input_email);
        //availability = (TextView)  findViewById(R.id.input_availability);
        //price = (TextView) findViewById(R.id.input_price);
        spin_prices = (Spinner) findViewById(R.id.spin_price);




        /* Date */


        /* Prices */

        List<String> prices = new ArrayList<>();


        // populate with the hours and minutes
        // time interval of 10 minutes
        for(double a = 0;a<=1;a+=0.1){
                prices.add(String.format("£%.2f",a));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(register.this, android.R.layout.simple_spinner_item, prices);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spin_prices.setAdapter(dataAdapter);
        /* ******** Button Listeners ******** */

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                startActivity(new Intent(register.this, login.class));
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getPostCode().execute(Common.apiRequest(String.valueOf(postData.getText())));
            }
        });

        /* ******** Registration to database  ******** */

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(register.this);
                pd.setTitle("Validating...");
                pd.show();


                // get the current ID registered
                DatabaseReference mDatabasePlayers = FirebaseDatabase.getInstance().getReference().child("users");
                mDatabasePlayers.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){

                        int key = 0; // contains the current key

                        for(DataSnapshot s: dataSnapshot.getChildren()){
                            key = Integer.parseInt(s.getKey());
                        }
                        key = key +1 ;
                        //String key1 = String.valueOf(key);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        // get the spinner info into text
                        String text = spinner.getSelectedItem().toString();
                        String[] addressLine1 = text.split(","); // addressline1[0] for first line
                        // get the name
                        String name = myName.getText().toString();
                        // get the email
                        String email = myEmail.getText().toString();
                        // get the name
                        //String avai = availability.getText().toString(); // needs to change for double spinners each with

                        // get the price
                        String priceHour = spin_prices.getSelectedItem().toString();
                        priceHour = priceHour.substring(1, priceHour.length());


                        DatabaseReference usersName = database.getReference("users")
                                .child(String.valueOf(key))
                                .child("Name");
                        usersName.setValue(name);


                        DatabaseReference usersEmail = database.getReference("users")
                                .child(String.valueOf(key))
                                .child("Email");
                        usersEmail.setValue(email);

                        DatabaseReference usersAddress = database.getReference("users")
                                .child(String.valueOf(key))
                                .child("Address");
                        usersAddress.setValue(addressLine1[0]);

                        DatabaseReference slotAvailabilityFrom = database.getReference("slot")
                                .child(addressLine1[0])
                                .child("availability")
                                .child(input_date.getText().toString())
                                .child("from_time");
                                slotAvailabilityFrom.setValue(input_time_from.getText().toString());

                        DatabaseReference slotAvailabilityTill = database.getReference("slot")
                                .child(addressLine1[0])
                                .child("availability")
                                .child(input_date.getText().toString())
                                .child("till_time");
                        slotAvailabilityTill.setValue(input_time_till.getText().toString());

                        DatabaseReference slotPrice = database.getReference("slot")
                                .child(addressLine1[0])
                                .child("price");
                        slotPrice.setValue(priceHour);

                        DatabaseReference slotOutcode = database.getReference("slot")
                                .child(addressLine1[0])
                                .child("postcode");
                        String outcode = postData.getText().toString();

                        slotOutcode.setValue(outcode.substring(0, outcode.length() - 3).toUpperCase());

                        pd.dismiss();

                        startActivity(new Intent(register.this,register_username.class));
                        finish();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException(); // don't swallow errors
                    }
                });
            }
        });
    } // END OF ONCREATE



    private class getPostCode extends AsyncTask<String,Void,String>{
        ProgressDialog pd = new ProgressDialog(register.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Looking for postcode...");
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
                Toast.makeText(register.this, String.format("Postcode not found"),
                        Toast.LENGTH_LONG).show();
                return;
            }

            // get the json file
            Gson gson = new Gson();

            //use reflection to reflect the json file to entities
            Type mType = new TypeToken<Location>(){}.getType();
            postcode = gson.fromJson(result,mType);
            System.out.print(postcode);


            System.out.println("+++++++++TEST" + postcode.getAddress());
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(register.this, android.R.layout.simple_spinner_item, postcode.getAddress());

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
            Toast.makeText(register.this, String.format("Found .%d slots available", postcode.getAddress().size()),
                    Toast.LENGTH_LONG).show();
            pd.dismiss();

            //System.out.println(postcode.getLatitude());
            //System.out.println(postcode.getAddress());
        }
    }




    public void selectDate(View view) {
        DialogFragment newFragment = new register.SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        input_date = (TextView)findViewById(R.id.input_date);
        input_date.setText(String.valueOf(String.format("%d-%d-%d",day,month,year)));
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



    public void selectFromTime(View view) {
        DialogFragment newFragment = new register.SelectFromTimeFragment();
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
            input_time_from = (TextView)findViewById(R.id.input_time_from);
            if(minute<10){input_time_from.setText(hour+":0"+minute);}
            else{input_time_from.setText(hour+":"+minute);}
        }
    }


    public void selectTillTime(View view) {
        DialogFragment newFragment = new register.SelectTillTimeFragment();
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
            input_time_till = (TextView)findViewById(R.id.input_time_till);
            if(minute<10){input_time_till.setText(hour+":0"+minute);}
            else{input_time_till.setText(hour+":"+minute);}
        }
    }
}
