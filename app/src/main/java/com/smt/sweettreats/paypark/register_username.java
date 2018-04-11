package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register_username extends AppCompatActivity {

    private Button next;
    private TextView myUsr,myPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_username);

        next = (Button) findViewById(R.id.btn_user);
        myUsr = (TextView) findViewById(R.id.input_username);
        myPw = (TextView) findViewById(R.id.input_password);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the current ID registered
                DatabaseReference mDatabasePlayers = FirebaseDatabase.getInstance().getReference().child("users");
                mDatabasePlayers.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int key = 0; // contains the current key

                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            key = Integer.parseInt(s.getKey());
                        }
                        //String key1 = String.valueOf(key);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        // get the spinner info into text

                        // get the username
                        String username = myUsr.getText().toString();
                        // get the password
                        String password = myPw.getText().toString();


                        DatabaseReference loginPw = database.getReference("login")
                                .child(username)
                                .child("password");
                        loginPw.setValue(password);

                        DatabaseReference loginValue = database.getReference("login")
                                .child(username)
                                .child("ID");
                        loginValue.setValue(key);
                        String address = (String) getIntent().getSerializableExtra("address");

                        DatabaseReference loginAddress= database.getReference("login")
                                .child(username)
                                .child("address");
                        loginAddress.setValue(address);
                        Toast.makeText(register_username.this, String.format("User registered"),
                                Toast.LENGTH_LONG).show();



                        // myUsr.setText(key1);

                        startActivity(new Intent(register_username.this, success.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}

