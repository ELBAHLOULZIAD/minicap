
package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.database.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Integer imgid[]={R.drawable.loft,R.drawable.sintex,R.drawable.loft};

    /**
     * added by Osama for cloud-app connection
     **/

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    final DatabaseReference refChild = databaseReference.child("Test").child("Stream").child("String");

    String stringWaterLevel;
    String intWaterLevel;
    Integer waterLevel;
   static Integer prev;

    /**
     * .......................................
     **/

    protected ListView listviewitems;//listview to list all tanks

    protected FloatingActionButton actionbutton;
    protected Button selectbutton;
    ArrayList<String> tanksListtext = new ArrayList<>();//array to list tanks in cm's
    ArrayList<String> tanksListtextinch = new ArrayList<>();//array to list tanks in inches
    int e = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listviewitems = findViewById(R.id.listviewitems);
        actionbutton = findViewById(R.id.actionbutton);
        selectbutton = findViewById(R.id.selectbutton);

        selectbutton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openactivityselect();
                                            }
                                        }
        );



        actionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//once insert button is clicked it runs a new fragment called insert tank frag.

                inserttankfrag dialog = new inserttankfrag();
                dialog.show(getSupportFragmentManager(), "Insert Tank");
            }


        });

        loadlistview();
        listviewitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//the selection from listview, once you click on any tank
                // Intent intent = new Intent(MainActivity.this, tankview.class);
                Intent intent = new Intent(MainActivity.this, tankview.class);//running the tankview activity

                String tankselected = tanksListtext.get(position);//locating the position where it will pass the tank name and tank height
                intent.putExtra("selectedd", tankselected);
                startActivity(intent);


            }
        });


    }

    ;

    public void openactivityselect(){
        Intent intent= new Intent(this, selecttank.class);
        startActivity(intent);}



    public boolean onCreateOptionsMenu(Menu menu) {//creates the drop down menu with the item for conversion
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item1) {
        if (item1.isEnabled() & e == 1) { //whenever the value is 1, it means that its number and convert it to letters by calling the arraygrade that is inserted into arrayadapter

//to list the tanks in cms
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tanksListtext);
            String list;
            listviewitems.setAdapter(arrayAdapter);

            e = 0;//before returning set e=0 so the next time it will alternate to the numeric array
            return false;
        } else if (item1.isEnabled() & e == 0) { //whenever the value is 0, it means that its number and convert it to letters by calling the arraygrade that is inserted into arrayadapter
//to list the tanks in inches
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tanksListtextinch);
            listviewitems.setAdapter(arrayAdapter);

            e = 1;//before exit set e to 1 so it will enter the If above condition
            return false;
        }
        return false;
    }

    protected void loadlistview() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
//listing all tanks
        final List<Tank> tanks = dbHelper.getAllTanks();


        /** added by Osama for cloud-app connection **/
        int check;
        check = isOnline();
        Toast.makeText(this, "The connection is " + check, Toast.LENGTH_LONG).show();
        if (check ==1) {
            refChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    stringWaterLevel = dataSnapshot.getValue(String.class);
                    intWaterLevel = stringWaterLevel.replaceAll("[^0-9]", "");
                    waterLevel = Integer.parseInt(intWaterLevel);
                    tanksListtext.clear();
                    tanksListtextinch.clear();
                    for (int i = 0; i < tanks.size(); i++) {
                        String temp = "";
                        String temp2 = "";
                        temp += tanks.get(i).getTitle() + "\n";


                        temp2 += tanks.get(i).getTitle() + "\n";

                        double y = Double.parseDouble(String.valueOf(tanks.get(i).getCode()));
                        double d = ((y - waterLevel)); // added by Osama for cloud-app connection
                        double z = y * 0.393701;
                        temp += "The height is: " + new DecimalFormat("##.##").format(y) + "cm\n";
                        temp2 += "The height is: " + new DecimalFormat("##.##").format(z) + "inches\n";
                        if (d >= 0) {
                            temp += "The level of water available is: " + new DecimalFormat("##.##").format((d / y) * 100) + "%";
                            temp2 += "The level of water available is: " + new DecimalFormat("##.##").format((d / y) * 100) + "%";
                        } else if (d < 0) {
                            temp += "The level of water available is: " + 0 + "%";
                            temp2 += "The level of water available is: " + 0 + "%";
                        }


                        tanksListtext.add(temp);
                        tanksListtextinch.add(temp2);

                    }
                prev=waterLevel;}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            /** ....................................... **/


            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tanksListtext);
            listviewitems.setAdapter(arrayAdapter);
            refresh(10000); //refresh
            if (e == 1)
                e = 0;

            }
         else if(check==0){
            Toast.makeText(this, "waterlevel saved is " + prev, Toast.LENGTH_LONG).show();
            if(prev==null)//running the app for the first time, we will assume that the sensor is reading 0
            {prev=0;}
            tanksListtext.clear();
            tanksListtextinch.clear();
            for (int i = 0; i < tanks.size(); i++) {
                String temp = "";
                String temp2 = "";
                temp += tanks.get(i).getTitle() + "\n";


                temp2 += tanks.get(i).getTitle() + "\n";

                double y = Double.parseDouble(String.valueOf(tanks.get(i).getCode()));
                double d = ((y - prev));
                double z = y * 0.393701;
                temp += "The height is: " + new DecimalFormat("##.##").format(y) + "cm\n";
                temp2 += "The height is: " + new DecimalFormat("##.##").format(z) + "inches\n";
                if (d >= 0) {
                    temp += "The level of water available is: " + new DecimalFormat("##.##").format((d / y) * 100) + "%";
                    temp2 += "The level of water available is: " + new DecimalFormat("##.##").format((d / y) * 100) + "%";
                } else if (d < 0) {
                    temp += "The level of water available is: " + 0 + "%";
                    temp2 += "The level of water available is: " + 0 + "%";
                }


                tanksListtext.add(temp);
                tanksListtextinch.add(temp2);

            }


            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tanksListtext);
            listviewitems.setAdapter(arrayAdapter);

            refresh(10000); //refresh
            if (e == 1)
                e = 0;

        }
       }


    public int isOnline() {
        boolean wifiConnected;
        boolean mobileConnected;
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()){ //connected with either mobile or wifi
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected){ //wifi connected
                return 1;
            }
            else if (mobileConnected){ //mobile data connected

                return 1;
            }
        }

        return 0;
    }



            //refresh function
private void refresh (int milliseconds)
{ final Handler handler= new Handler();
final Runnable runnable= new Runnable(){
    @Override
    public void run()
    {loadlistview();}
};
handler.postDelayed(runnable, milliseconds);

}






}
