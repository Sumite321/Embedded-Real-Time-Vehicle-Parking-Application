package com.smt.sweettreats.paypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;

import org.w3c.dom.Text;

public class LoginHome extends AppCompatActivity {

    private TextView username;
    private SessionManager session;
    private Button logout, viewBooking,viewWhoBooked,reserve,rent,pair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        session = MainActivity.session;



        username = (TextView) findViewById(R.id.sessionUser);
        username.setText(session.getUserDetails().get("userid") + session.getUserDetails().get("id"));
        logout = (Button) findViewById(R.id.btn_logout);
        reserve = (Button) findViewById(R.id.btn_reserve);
        rent = (Button) findViewById(R.id.btn_rent);
        viewBooking = (Button ) findViewById(R.id.btn_view);
        viewWhoBooked = (Button ) findViewById(R.id.btn_view2);
        pair = (Button ) findViewById(R.id.btn_pair);

        viewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginHome.this, DisplayBooking.class);
                intent.putExtra("isRentals",false);
                startActivity(intent);
            }
        });

        viewWhoBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginHome.this, DisplayBooking.class);
                intent.putExtra("isRentals",true);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginHome.this, ApiRadius.class);
                startActivity(intent);
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginHome.this, register.class);
                startActivity(intent);

            }
        });

        pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginHome.this, PairSensor.class);
                intent.putExtra("codeID","Unknown");

                startActivity(intent);

            }
        });

    }
}
