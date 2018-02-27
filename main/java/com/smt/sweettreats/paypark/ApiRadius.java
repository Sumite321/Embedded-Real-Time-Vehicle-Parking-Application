package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;

public class ApiRadius extends AppCompatActivity implements Serializable{

    private TextView text;
    private EditText filter,radius;
    Radius getRadius = new Radius();
    private ArrayList<String> filteredPostcode = new ArrayList<>();
    private Button showFiltered;
    public ArrayList<slot> rentalSlots = new ArrayList<>();
    private ArrayList<slot> showFilteredArray = new ArrayList<>();
    private ArrayList<String> availability;
    private Spinner spin_from,spin_till;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_postcode);

        text = (TextView) findViewById(R.id.postcode_search);
        showFiltered = (Button) findViewById(R.id.btn_showbooking);
        filter = (EditText) findViewById(R.id.input_postcode);
        radius = (EditText) findViewById(R.id.input_radius);
        spin_from = (Spinner) findViewById(R.id.drop_from);
        spin_till = (Spinner) findViewById(R.id.drop_till);

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
        System.out.println(showFilteredArray);

        List<String> hours = new ArrayList<>();


        // populate with the hours and minutes
        // time interval of 10 minutes
        for(int a = 0;a<=23;a++){
            for(int b = 00;b<=50;b+=10){
                if(b == 0){hours.add(String.valueOf(a)+":"+String.valueOf(b)+"0");}
                else{hours.add(String.valueOf(a)+":"+String.valueOf(b));}
            }
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ApiRadius.this, android.R.layout.simple_spinner_item, hours);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spin_from.setAdapter(dataAdapter);
        spin_till.setAdapter(dataAdapter);
    }



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
            intent.putExtra("fromTime",spin_from.getSelectedItem().toString());
            intent.putExtra("tillTime", spin_till.getSelectedItem().toString());
            startActivity(intent);
            finish();
            pd.dismiss();

            //System.out.println(postcode.getLatitude());
            //System.out.println(postcode.getAddress());
        }
    }
}


