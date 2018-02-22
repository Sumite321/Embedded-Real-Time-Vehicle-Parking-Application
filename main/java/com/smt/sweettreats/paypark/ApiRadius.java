package com.smt.sweettreats.paypark;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ApiRadius extends AppCompatActivity {

    private TextView text;

    Radius getRadius = new Radius();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_postcode);

        text = (TextView) findViewById(R.id.postcode_search);

        new ApiRadius.getPostCodeinRadius().execute(Common.apiRadius("HA0","5000"));

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
            System.out.print(getRadius);


            System.out.println("+++++++++TEST" + getRadius.getStatus());
            System.out.println(getRadius.getResult());
            for (Location l: getRadius.getResult()){
                System.out.println(l.getDistance());
                System.out.println(l.getOutcode());


            }

            // Creating adapter for spinner
            text.setText("ss");







            pd.dismiss();

            //System.out.println(postcode.getLatitude());
            //System.out.println(postcode.getAddress());
        }
    }
}


