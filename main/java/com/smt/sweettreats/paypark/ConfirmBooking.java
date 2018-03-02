package com.smt.sweettreats.paypark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmBooking extends AppCompatActivity {


    private TextView address,price,duration,from, till, date,total;
    private double priceTotal;
    private int time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);


        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        duration = (TextView) findViewById(R.id.duration);
        from = (TextView) findViewById(R.id.textFrom);
        till = (TextView) findViewById(R.id.textTill);
        date = (TextView) findViewById(R.id.textDate);
        total = (TextView) findViewById(R.id.textTotal);



        String addressData = getIntent().getExtras().getString("address");
        Double priceData = getIntent().getExtras().getDouble("price");
        String outcodeData = getIntent().getExtras().getString("outcode");
        String fromTime = getIntent().getExtras().getString("from");
        String tillTime = getIntent().getExtras().getString("till");
        String dateV = getIntent().getExtras().getString("date");

        try {
            time = differenceTime(fromTime, tillTime);
            priceTotal = (priceData/60)*time;
        }catch (ParseException e){
            // some error
        }

        address.setText(addressData);
        price.setText(priceData.toString());
        duration.setText(String.format("%d", time,"Minutes"));
        from.setText(fromTime);
        till.setText(tillTime);
        date.setText(dateV);
        total.setText(String.format("£ %.2f", priceTotal,"0"));



    }

    public int differenceTime(String from,String till) throws ParseException{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = simpleDateFormat.parse(from);
        Date endDate = simpleDateFormat.parse(till);

        long difference = endDate.getTime() - startDate.getTime();
        if(difference<0)
        {
            Date dateMax = simpleDateFormat.parse("24:00");
            Date dateMin = simpleDateFormat.parse("00:00");
            difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
        }
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        return 60*hours+min;
    }


}
