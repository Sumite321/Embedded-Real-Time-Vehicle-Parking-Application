package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ApiRadius extends AppCompatActivity implements Serializable{

    private TextView text;
    private EditText filter;
    Radius getRadius = new Radius();
    private ArrayList<String> filteredPostcode = new ArrayList<>();
    private Button showFiltered;
    public ArrayList<slot> rentalSlots = new ArrayList<>();
    private ArrayList<slot> showFilteredArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_postcode);

        text = (TextView) findViewById(R.id.postcode_search);
        showFiltered = (Button) findViewById(R.id.btn_showbooking);
        filter = (EditText) findViewById(R.id.input_postcode);

        showFilteredArray.clear();
        //Get datasnapshot at your "users" root node
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2, ref3, ref4;
        ref2 = ref1.child("slot");

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    double price =  Double.valueOf(dsp.child("price").getValue().toString());
                    String time = dsp.child("availability").getValue().toString();
                    String outcode = dsp.child("postcode").getValue().toString();
                    //if(filteredPostcode.contains(dsp.getKey())) {
                    rentalSlots.add(new slot(String.valueOf(dsp.getKey()), outcode,"London", "Available to book", price, "slot_image_1", 1, false));
                    //}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        showFiltered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ApiRadius.getPostCodeinRadius().execute(Common.apiRadius(filter.getText().toString(),"50"));
                //System.out.println(rentalSlots);
                //System.out.println(showFilteredArray);

            }

        });
        System.out.println(showFilteredArray);
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
            startActivity(intent);
            finish();
            pd.dismiss();

            //System.out.println(postcode.getLatitude());
            //System.out.println(postcode.getAddress());
        }
    }
}


