package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;

public class AboutYou extends AppCompatActivity implements Serializable{


    private TextView user,pw,email,name,postData;
    Location postcode = new Location();

    private Spinner spinner;
    private Button next,search;
    private ArrayList<String> slots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        name = (TextView)  findViewById(R.id.input_name);
        pw = (TextView)  findViewById(R.id.input_password);
        email = (TextView)  findViewById(R.id.input_email);
        user = (TextView)  findViewById(R.id.input_username);
        spinner = (Spinner) findViewById(R.id.spinner1);
        search = (Button) findViewById(R.id.btn_search);
        postData = (EditText) findViewById(R.id.input_address);


        next = (Button) findViewById(R.id.btn_nxt);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AboutYou.getPostCode().execute(Common.apiRequest(String.valueOf(postData.getText())));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference mDatabasePlayers = FirebaseDatabase.getInstance().getReference().child("users");
                mDatabasePlayers.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int key = 0; // contains the current key

                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            key = Integer.parseInt(s.getKey());
                        }
                        key = key + 1;
                        //String key1 = String.valueOf(key);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        // get the name
                        String name1 = name.getText().toString();
                        // get the email
                        String email1 = email.getText().toString();

                        /***** add to USER table ***/
                        DatabaseReference usersName = database.getReference("users")
                                .child(String.valueOf(key))
                                .child("Name");
                        usersName.setValue(name1);

                        DatabaseReference usersEmail = database.getReference("users")
                                .child(String.valueOf(key))
                                .child("Email");
                        usersEmail.setValue(email1);

                        String text = spinner.getSelectedItem().toString();
                        String[] addressLine1 = text.split(","); // addressline1[0] for first line

                        /**** ADD TO LOGIN TABLE ****/
                        DatabaseReference loginUsr = database.getReference("login")
                                .child(user.getText().toString());

                        DatabaseReference loginPw = database.getReference("login")
                                .child(user.getText().toString())
                                .child("password");
                        loginPw.setValue(pw.getText().toString());

                        DatabaseReference loginValue = database.getReference("login")
                                .child(user.getText().toString())
                                .child("ID");
                        loginValue.setValue(String.valueOf(key));

                        DatabaseReference loginAdd = database.getReference("login")
                                .child(user.getText().toString())
                                .child("address");
                        loginAdd.setValue(addressLine1[0]);

                        slots = (ArrayList<String>) getIntent().getSerializableExtra("slotD");

                        Intent intent = new Intent(AboutYou.this, MakeBooking.class);
                        //intent.putExtra("streetNumber", slot.getStreetNumber());
                        intent.putExtra("userID", key); //address bookable
                        intent.putExtra("slotD", slots);
                        intent.putExtra("userName", name.getText().toString());
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
}

    private class getPostCode extends AsyncTask<String,Void,String> {
        ProgressDialog pd = new ProgressDialog(AboutYou.this);


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
                Toast.makeText(AboutYou.this, String.format("Postcode not found"),
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
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AboutYou.this, android.R.layout.simple_spinner_item, postcode.getAddress());

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
            Toast.makeText(AboutYou.this, String.format("Found %d postcode", postcode.getAddress().size()),
                    Toast.LENGTH_LONG).show();
            pd.dismiss();

            //System.out.println(postcode.getLatitude());
            //System.out.println(postcode.getAddress());
        }
    }


}
