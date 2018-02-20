package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;


public class register extends AppCompatActivity {


    String stream = null;
    Location postcode = new Location();
    Common common = new Common();
    String mpostData = "NW44SG";
    private EditText postData;
    Spinner dropdown;
    private Button search,next;
    public Spinner spinner;
    private TextView loginLink, myName, myEmail, availability, price;

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
        availability = (TextView)  findViewById(R.id.input_availability);
        price = (TextView) findViewById(R.id.input_price);


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
                // get the current ID registered
                DatabaseReference mDatabasePlayers = FirebaseDatabase.getInstance().getReference().child("users");
                mDatabasePlayers.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        String avai = availability.getText().toString();
                        // get the email
                        String priceHour = price.getText().toString();

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

                        DatabaseReference slotAvailability = database.getReference("slot")
                                .child(addressLine1[0])
                                .child("availability");
                        slotAvailability.setValue(avai);

                        DatabaseReference slotPrice = database.getReference("slot")
                                .child(addressLine1[0])
                                .child("price");
                        slotPrice.setValue(priceHour);
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






            pd.dismiss();

            //System.out.println(postcode.getLatitude());
            //System.out.println(postcode.getAddress());
        }
    }
}
