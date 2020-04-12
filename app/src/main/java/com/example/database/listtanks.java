package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class listtanks extends AppCompatActivity {
    protected ListView listviewitems;//listview to list all tanks
    ArrayList<String> tanksListtext = new ArrayList<>();//array to list tanks in cm's
    ArrayList<String> tanksListtextinch = new ArrayList<>();//array to list tanks in inches

    Integer waterLevel;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    //
    private FirebaseAuth firebaseAuth;


    final DatabaseReference refChild = databaseReference.child("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listtanks);
        listviewitems = findViewById(R.id.listviewitems);

            listviewitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//the selection from listview, once you click on any tank
                // Intent intent = new Intent(MainActivity.this, tankview.class);
                Intent intent = new Intent(listtanks.this, tankview.class);//running the tankview activity

                String tankselected = tanksListtext.get(position);//locating the position where it will pass the tank name and tank height
                intent.putExtra("selectedd", tankselected);
                startActivity(intent);


            }
        });





    }

    protected void loadlistviews() {

        try {
            tanksListtext.clear();
            tanksListtextinch.clear();
            refChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference objRef = refChild.child(childDataSnapshot.getKey());
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        //taskMap.put("is_read", "1");
                        Log.v("Testing", "" + childDataSnapshot.getKey());
                        if (childDataSnapshot.getKey().equals(MainActivity.name)) {
                            DatabaseReference refChild2 = databaseReference.child("users").child(childDataSnapshot.getKey());
                            for (DataSnapshot childDataSnapshot2 : dataSnapshot.child(childDataSnapshot.getKey()).getChildren()) {
                                DatabaseReference objRef2 = refChild2.child(childDataSnapshot2.getKey());
                                Map<String, Object> taskMap2 = new HashMap<String, Object>();
                                Log.v("Testing2", "" + childDataSnapshot2.getKey());
                                DatabaseReference refChild3 = databaseReference.child("users").child(childDataSnapshot.getKey()).child(childDataSnapshot2.getKey());
                                // for (DataSnapshot childDataSnapshot3 : dataSnapshot.child(childDataSnapshot.getKey()).child(childDataSnapshot2.getKey()).getChildren()) {
//                               DatabaseReference objRef3 = refChild3.child(childDataSnapshot3.getKey());
//                               Map<String, Object> taskMap3 = new HashMap<String, Object>();
//                               Log.v("Testing3", "" + childDataSnapshot3.getKey());
                                String height = childDataSnapshot2.child("Height").getValue().toString();
                                String readings = childDataSnapshot2.child("Readings").getValue().toString();
                                String notificat = childDataSnapshot2.child("notification").getValue().toString();
                                Log.v("height", "The Tank name is " + childDataSnapshot2.getKey() + " height is " + height + ",Reading is " + readings + ", Notification is" + notificat + "\n");

                               // firebaseDatabase.getReference().child("users").child(Mainname).child("ziad").removeValue();
//                               Log.v("Testing3", "" + firebaseDatabase.getReference(childDataSnapshot2.getKey()).toString());
                                waterLevel = Integer.parseInt(readings);
                                String temp = "";
                                String temp2 = "";
                                temp += childDataSnapshot2.getKey() + "\n";
                                temp2 += childDataSnapshot2.getKey() + "\n";
                                double y = Double.parseDouble(String.valueOf(height));
                                double d = ((y - waterLevel));
                                double z = y * 0.393701;
                                temp += "The height is: " + new DecimalFormat("##.##").format(y) + "cm\n";

                                temp2 += "The height is: " + new DecimalFormat("##.##").format(z) + "inches\n";
                                if (d >= 0) {
                                    temp += "The level of water available is: " + new DecimalFormat("##.##").format((d / y) * 100) + "%\n";
                                    temp += "The notification is set to: " + notificat + "\n";
                                    temp2 += "The level of water available is: " + new DecimalFormat("##.##").format((d / y) * 100) + "%\n";
                                    temp2 += "The notification is set to: " + notificat + "\n";
                                } else if (d < 0) {
                                    temp += "The level of water available is: " + 0 + "%\n";
                                    temp += "The notification is set to: " + notificat + "\n";
                                    temp2 += "The level of water available is: " + 0 + "%\n";
                                    temp2 += "The notification is set to: " + notificat + "\n";

                                }
                                tanksListtext.add(temp);
                                tanksListtextinch.add(temp2);
                            }
                        }
                        if (MainActivity.e == 0) {
                            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(listtanks.this, android.R.layout.simple_list_item_1, tanksListtext);
                            listviewitems.setAdapter(arrayAdapter);
                        }
                        if (MainActivity.e == 1) {
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(listtanks.this, android.R.layout.simple_list_item_1, tanksListtextinch);
                            listviewitems.setAdapter(arrayAdapter);
                        }


                        ; //should I use setValue()...?
                        //Log.v("Testing",""+ childDataSnapshot.getKey()); //displays the key for the node
                        //Log.v("Testing",""+ childDataSnapshot.child().getKey());
                        //   refresh(10000); //refresh
                    }


                    //Toast.makeText(MainActivity.this, "The snapshot is "+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                    System.out.println(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });


        }
        catch (Exception ex) {
            // System.out.println(ex.toString());
            Toast.makeText(listtanks.this, "The error is "+ex.toString(), Toast.LENGTH_LONG).show();
        }






    }
    protected void onStart() {
        super.onStart();
        loadlistviews();
    }
}
