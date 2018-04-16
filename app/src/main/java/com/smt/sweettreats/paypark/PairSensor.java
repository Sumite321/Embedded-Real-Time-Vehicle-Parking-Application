package com.smt.sweettreats.paypark;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.xml.transform.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler;

public class PairSensor extends AppCompatActivity implements ResultHandler {

    // this class should take the ID of the sensor and apply to the account and slot chosen

    private ZXingScannerView mScannerView;
    private EditText sensorID;
    private TextView scannedCode;
    private Button connect;
    private String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_sensor);


        myID = getIntent().getExtras().getString("codeID");

        sensorID = (EditText) findViewById(R.id.enter_sensor);
        scannedCode = (TextView) findViewById(R.id.scannedCode);
        connect = (Button) findViewById(R.id.btn_nextPage);
        scannedCode.setText(myID);
        sensorID.setText(myID);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PairSensor.this, PairSensor.class);
                intent.putExtra("codeID",scannedCode.getText());
                startActivity(intent);

            }
        });

    }

    public void QrScanner(View view) {
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }


    @Override
    public void handleResult(com.google.zxing.Result result) {
        // Do something with the result here
        Log.e(";handler ", result.getText()); // Prints scan results
        Log.e(";handler ", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // show the scanner result into dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(";Scan Result ");
        builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();

        Intent intent = new Intent(PairSensor.this, PairSensor.class);
        intent.putExtra("codeID",result.getText());
        startActivity(intent);
        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }
}
