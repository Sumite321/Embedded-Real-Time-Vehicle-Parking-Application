package com.smt.sweettreats.paypark;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

import java.io.Serializable;
import java.util.ArrayList;

public class AboutYou extends AppCompatActivity implements Serializable{


    private TextView user,pw,email,name;
    private Button next;
    private ArrayList<String> slots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        name = (TextView)  findViewById(R.id.input_name);
        pw = (TextView)  findViewById(R.id.input_password);
        email = (TextView)  findViewById(R.id.input_email);
        user = (TextView)  findViewById(R.id.input_username);

        next = (Button) findViewById(R.id.btn_nxt);


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
}
