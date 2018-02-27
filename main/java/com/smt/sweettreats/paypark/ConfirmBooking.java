package com.smt.sweettreats.paypark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConfirmBooking extends AppCompatActivity {


    private TextView address,price,duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);


        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        duration = (TextView) findViewById(R.id.duration);



        String addressData = getIntent().getExtras().getString("address");
        Double priceData = getIntent().getExtras().getDouble("price");
        String outcodeData = getIntent().getExtras().getString("outcode");

        address.setText(addressData);
        price.setText(priceData.toString());
        duration.setText(outcodeData);


    }
}
