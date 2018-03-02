package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.widget.Toast;

public class login extends AppCompatActivity {


    private Button login;
    private TextView tv;
    private DatabaseReference ref;
    private ProgressDialog mProgress;
    private EditText edit_usr, edit_pw;
    public static SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();

        mProgress = new ProgressDialog(login.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        login = (Button) findViewById(R.id.btn_login);
        //tv = (TextView) findViewById(R.id.link_signup);
        edit_usr = (EditText) findViewById(R.id.edit_user);
        edit_pw = (EditText) findViewById(R.id.edit_pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRef = database.getReference("login");
                userRef.child(edit_usr.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getValue() != null && snapshot.child("password").getValue().toString().equals(edit_pw.getText().toString())) {
                            //user exists, do something
                            //tv.setText("You are logged in");


                            mProgress.setMessage("You are logged in!");
                            session.createLoginSession(edit_usr.getText().toString(),"email");


                            Toast.makeText(login.this, "You are logged in",
                                    Toast.LENGTH_LONG).show();
                            mProgress.setCancelable(true);

                            Intent intent = new Intent(login.this,LoginHome.class);
                            startActivity(intent);
                            finish();
                        } else {
                            mProgress.setMessage("You are not logged in!");
                            mProgress.setCancelable(true);
                            Toast.makeText(login.this, "Check your credentials",
                                    Toast.LENGTH_LONG).show();

                            //user does not exist, do something else
                           // tv.setText("You are not logged in");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        });

            }




    }
