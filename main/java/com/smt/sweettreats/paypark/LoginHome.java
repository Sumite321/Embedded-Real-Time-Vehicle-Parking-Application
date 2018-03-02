package com.smt.sweettreats.paypark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginHome extends AppCompatActivity {

    private TextView username;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        session = login.session;


        
        username = (TextView) findViewById(R.id.sessionUser);
        username.setText(session.getUserDetails().get("userid"));




    }
}
