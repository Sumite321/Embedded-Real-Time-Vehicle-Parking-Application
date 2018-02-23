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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class bookingListView extends AppCompatActivity implements Serializable{

    public ArrayList<slot> rentalSlots = new ArrayList<>();
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list_view);



        rentalSlots = (ArrayList<slot>) getIntent().getSerializableExtra("filteredPostcode");

        System.out.print("asd");
        System.out.print(rentalSlots);

        //create our slot elements
        //rentalSlots.add(new slot(10, "Smith Street", "Sydney", "NSW", "A large 3 bedroom apartment right in the heart of Sydney! A rare find, with 3 bedrooms and a secured car park.", 450.00, "slot_image_1", 3, 1, 1, false));
        //rentalSlots.add(new slot(66, "King Street", "Sydney", "NSW", "A fully furnished studio apartment overlooking the harbour. Minutes from the CBD and next to transport, this is a perfect set-up for city living.", 320.00, "slot_image_2", 1, 1, 1, false));
        //rentalSlots.add(new slot(1, "Liverpool Road", "Liverpool", "NSW", "A standard 3 bedroom house in the suburbs. With room for several cars and right next to shops this is perfect for new families.", 360.00, "slot_image_3", 3, 2, 2, true));
        //rentalSlots.add(new slot(567, "Sunny Street", "Gold Coast", "QLD", "Come and see this amazing studio apartment in the heart of the gold coast, featuring stunning waterfront views.", 360.00, "slot_image_4" , 1, 1, 1, false));


       
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

                Intent intent = new Intent(bookingListView.this, success.class);
                //intent.putExtra("streetNumber", slot.getStreetNumber());
                intent.putExtra("streetName", slot.getStreetName());
                intent.putExtra("suburb", slot.getSuburb());
                //intent.putExtra("state", slot.getState());
                intent.putExtra("image", slot.getImage());

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


            TextView description = (TextView) view.findViewById(R.id.description);
            TextView address = (TextView) view.findViewById(R.id.address);
            TextView bedroom = (TextView) view.findViewById(R.id.bedroom);
            TextView bathroom = (TextView) view.findViewById(R.id.bathroom);
            TextView carspot = (TextView) view.findViewById(R.id.carspot);
            TextView price = (TextView) view.findViewById(R.id.price);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            //set address and description
            String completeAddress =  " " + slot.getStreetName() + ", " + slot.getSuburb() + ", ";
            address.setText(completeAddress);

            //display trimmed excerpt for description
            int descriptionLength = slot.getDescription().length();
            if(descriptionLength >= 100){
                String descriptionTrim = slot.getDescription().substring(0, 100) + "...";
                description.setText(descriptionTrim);
            }else{
                description.setText(slot.getDescription());
            }

            //set price and rental attributes
            price.setText("$" + String.valueOf(slot.getPrice()));
            //bedroom.setText("Bed: " + String.valueOf(slot.getBedrooms()));
            //bathroom.setText("Bath: " + String.valueOf(slot.getBathrooms()));
            carspot.setText("Car: " + String.valueOf(slot.getCarspots()));

            //get the image associated with this slot
            int imageID = context.getResources().getIdentifier(slot.getImage(), "drawable", context.getPackageName());
            image.setImageResource(imageID);

            return view;
        }
    }

}
