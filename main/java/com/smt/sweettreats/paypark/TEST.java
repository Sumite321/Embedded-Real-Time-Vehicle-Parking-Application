package com.smt.sweettreats.paypark;

/**
 * Created by admin on 02/03/2018.
 */

public class TEST {

/*

    DatabaseReference ref5 = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref6;
    ref6 = ref5.child("booking");
                ref6.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataS) {
            System.out.println("Working");

            System.out.println(dataS.child(input_booking_date.getText().toString()).exists());
            // validation
            availability.add("bullcrap");

            // if the table has the date
            if (dataS.child(input_booking_date.getText().toString()).exists()) {

                // run the loop if the date exists
                //contains all the addresses for the date requested
                for (DataSnapshot dsp : dataS.child(input_booking_date.getText().toString()).getChildren()) {
                    System.out.println("Working");
                    System.out.println(dsp.getKey().equals(input_booking_date.getText().toString()));
                    System.out.println(dsp.child(input_booking_date.getText().toString()).getValue() != null);
                    System.out.println(input_booking_date.getText().toString());
                    System.out.println(dsp.getValue().toString());//
                    line1 = dsp.getKey(); // has the address

                    // loop through all the bookingID
                    for (DataSnapshot bookingID : dsp.getChildren()) {
                        //loop here will go through all the addresses
                        System.out.println(dsp.getKey()); // has the ID
                        String from = bookingID.child("from_time").getValue().toString();
                        String till = bookingID.child("till_time").getValue().toString();
                        // if checkTimings
                        if (checktimings(from, booking_time_f.getText().toString())
                                && !checktimings(till, booking_time_t.getText().toString())) {
                            availability.add(line1);
                        }
                    }
                }
            }
            System.out.println(availability.toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }); // end of ref6 listener*/
}
