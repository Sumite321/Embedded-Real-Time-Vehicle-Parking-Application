package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class register extends AppCompatActivity {


    String stream = null;
    Location postcode = new Location();
    Common common = new Common();
    String postData = "NW44SG";

    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        loginLink = (TextView) findViewById(R.id.link_login);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity

                startActivity(new Intent(register.this, login.class));
                finish();
            }
        });



        new GetWeather().execute(Common.apiRequest(String.valueOf(postData)));

        //loginLink.setText(a);
    }

    private class GetWeather extends AsyncTask<String,Void,String>{
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
            // dismiss progress dialog and update ui


            super.onPostExecute(result);
            if(result.contains("Error: Not found city")){
                pd.dismiss();
                return;
            }
            Gson gson = new Gson();
            Type mType = new TypeToken<Location>(){}.getType();
            postcode = gson.fromJson(result,mType);
            pd.dismiss();

            //System.out.println(postcode.getLatitude());
            //System.out.println(postcode.getAddress());

            Spinner dropdown = findViewById(R.id.spinner1);
//create a list of items for the spinner.
            String[] items = new String[]{"1", "2", "three"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
            dropdown.setAdapter(adapter);


        }
    }






}
