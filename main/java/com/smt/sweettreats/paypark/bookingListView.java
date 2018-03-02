package com.smt.sweettreats.paypark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class bookingListView extends AppCompatActivity implements Serializable{

    public ArrayList<slot> rentalSlots = new ArrayList<>();
    private ListView mListView;
    private boolean available;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list_view);



        rentalSlots = (ArrayList<slot>) getIntent().getSerializableExtra("filteredPostcode");
        final String fromTime = getIntent().getExtras().getString("from");
        final String tillTime = getIntent().getExtras().getString("till");
        final String date = getIntent().getExtras().getString("date");



        //create our new array adapter
        ArrayAdapter<slot> adapter = new slotArrayAdapter(this, 0, rentalSlots);

        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.customListView);
        listView.setAdapter(adapter);


        //add event listener so we can handle clicks
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                slot slot = rentalSlots.get(position);

                Intent intent = new Intent(bookingListView.this, ConfirmBooking.class);
                //intent.putExtra("streetNumber", slot.getStreetNumber());
                intent.putExtra("address", slot.getStreetName()); //address bookable
                intent.putExtra("outcode", slot.getOutcode()); // postcode
                intent.putExtra("price",slot.getPrice()); // price per hour for booking
                intent.putExtra("from", date); //address bookable
                intent.putExtra("till", fromTime); // postcode
                intent.putExtra("date",tillTime); // price per hour for booking
                System.out.println("asdsd");
                System.out.println(slot.getStreetName());
                //intent.putExtra("state", slot.getState());
                intent.putExtra("image", slot.getImage());  // optional

                startActivityForResult(intent, 1000);
            }
        };
        //set the listener to the list view
        listView.setOnItemClickListener(adapterViewListener);


    }

    //custom ArrayAdapater
    class slotArrayAdapter extends ArrayAdapter<slot>{

        private Context context;
        private List<slot> rentalSlots;

        //constructor, call on creation
        public slotArrayAdapter(Context context, int resource, ArrayList<slot> objects) {
            super(context, resource, objects);

            this.context = context;
            this.rentalSlots = objects;
        }

        //called when rendering the list
        public View getView(int position, View convertView, ViewGroup parent) {

            //get the slot we are displaying
            slot slot = rentalSlots.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //conditionally inflate either standard or special template
            View view;
            if(slot.getFeatured() == true){
                view = inflater.inflate(R.layout.slot_layout_alt, null);
            }else{
                view = inflater.inflate(R.layout.slot_layout, null);
            }


            TextView description = (TextView) view.findViewById(R.id.description);// format From,Till -> [13,19]
            TextView address = (TextView) view.findViewById(R.id.address);
            //TextView bedroom = (TextView) view.findViewById(R.id.bedroom);
            //TextView bathroom = (TextView) view.findViewById(R.id.bathroom);
            TextView carspot = (TextView) view.findViewById(R.id.carspot);
            TextView price = (TextView) view.findViewById(R.id.price);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            //set address and description
            String completeAddress =  " " + slot.getStreetName() + ", " + slot.getSuburb() + ", ";
            address.setText(completeAddress);


            //display trimmed excerpt for description

            description.setText("WIll contain sensor information");


            //set price and rental attributes
            price.setText("$" + String.valueOf(slot.getPrice()));
            //bedroom.setText("Bed: " + String.valueOf(slot.getBedrooms()));
            //bathroom.setText("Bath: " + String.valueOf(slot.getBathrooms()));
            carspot.setText("Car: " + String.valueOf(slot.getCarspots()));

            //get the image associated with this slot
            //int imageID = context.getResources().getIdentifier(slot.getImage(), "drawable", context.getPackageName());
            image.setImageResource(R.drawable.driveway);

            return view;
        }
    }

}
