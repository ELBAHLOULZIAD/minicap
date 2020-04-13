package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class listtanks extends AppCompatActivity {
    public ListView listviewitems;//listview to list all tanks
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
        tanksListtext.clear();
        tanksListtextinch.clear();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("users").child(MainActivity.name).getKey();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //snapshot.child("users").child(MainActivity.name).getChildren().toString() ;

                Toast.makeText(listtanks.this, "snashot" + snapshot, Toast.LENGTH_SHORT).show();
                //Log.v("snashot", "" + snapshot.child("users").child(MainActivity.name).getChildren() );
                Log.v("snano", "" + snapshot.child("users").child(MainActivity.name).getValue());
                //  Log.v("snano", "" + snapshot.getValue().toString());

//                 snapshot.child("users").child(MainActivity.name).getValue().toString();
                for (DataSnapshot childDataSnapshot : snapshot.child("users").child(MainActivity.name).getChildren()) {

                    Log.v("name of tank", "" + childDataSnapshot.getKey());
                    Log.v("snashot", "" + childDataSnapshot.child("Height").getValue().toString());
                    Log.v("notificat", "" + childDataSnapshot.child("notification").getValue().toString());

                    Log.v("readings", "" + childDataSnapshot.child("Readings").getValue().toString());

                    String height = childDataSnapshot.child("Height").getValue().toString();
                    String readings = childDataSnapshot.child("Readings").getValue().toString();
                    String notificat = childDataSnapshot.child("notification").getValue().toString();

                    waterLevel = Integer.parseInt(readings);
                    String temp = "";
                    String temp2 = "";
                    temp += childDataSnapshot.getKey() + "\n";
                    temp2 += childDataSnapshot.getKey() + "\n";
                    double y = Double.parseDouble(String.valueOf(height));
                    double d = ((y - waterLevel));
                    double z = y * 0.393701;
                    String nnot = "OFF";
                    int setnot = Integer.parseInt((notificat));
                    if (setnot == 1) {
                        nnot = "Quarter";
                    }
                    if (setnot == 2) {
                        nnot = "Half";
                    }
                    if (setnot == 3) {
                        nnot = "Three Quarter";
                    }
                    if (setnot == 4) {
                        nnot = "OFF";
                    }

                    temp += "The height is: " + new DecimalFormat("##.##").format(y) + "cm\n";

                    temp2 += "The height is: " + new DecimalFormat("##.##").format(z) + "inches\n";
                    if (d >= 0) {
                        temp += "The level of water available is: " + new DecimalFormat("##.##").format((d / y) * 100) + "%\n";
                        temp += "The notification is set to:" + nnot + "\n";
                        temp2 += "The level of water available is: " + new DecimalFormat("##.##").format((d / y) * 100) + "%\n";
                        temp2 += "The notification is set to:" + nnot + "\n";
                    } else if (d < 0) {
                        temp += "The level of water available is: " + 0 + "%\n";
                        temp += "The notification is set to:" + nnot + "\n";
                        temp2 += "The level of water available is: " + 0 + "%\n";
                        temp2 += "The notification is set to:" + nnot + "\n";
                    }
                    tanksListtext.add(temp);
                    tanksListtextinch.add(temp2);
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


                //Toast.makeText(MainActivity.this, "The snapshot is "+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                //    System.out.println(dataSnapshot.getValue());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
    protected void onStart() {
        super.onStart();
       loadlistviews();
    }
}
