
package com.example.database;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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

import static android.app.NotificationManager.IMPORTANCE_HIGH;

//import static androidx.core.app.NotificationCompat.Builder;

public class MainActivity extends AppCompatActivity  {
    Integer imgid[] = {R.drawable.loft, R.drawable.sintex, R.drawable.loft};

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



   static  int noti = 1;//notification default value is false
      static  int id=1;
     static   int swt=0;
static int can=0;
    Editable notifi;
    static int ref=100000;//default value for notification recurrence
    private NotificationManager mNotifyManager;
    private Notification.Builder mBuilder;


    /**
     * .......................................
     **/

    protected ListView listviewitems;//listview to list all tanks

    protected FloatingActionButton actionbutton;
    protected Button selectbutton;
    protected FloatingActionButton actionbutton2;
    protected TextView textView;
    ArrayList<String> tanksListtext = new ArrayList<>();//array to list tanks in cm's
    ArrayList<String> tanksListtextinch = new ArrayList<>();//array to list tanks in inches
    int e = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        listviewitems = findViewById(R.id.listviewitems);
        actionbutton = findViewById(R.id.actionbutton);
        selectbutton = findViewById(R.id.selectbutton);
        actionbutton2 = findViewById(R.id.actionbutton2);
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
        actionbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//once insert button is clicked it runs a new fragment called insert tank frag.

                confnoti dialog = new confnoti();
                dialog.show(getSupportFragmentManager(), "Config");
            }


        });
        loadlistview();
        timerrrr();

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

    public void openactivityselect() {
        Intent intent = new Intent(this, selecttank.class);

        startActivity(intent);
    }


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
        //Toast.makeText(this, "The connection is " + check, Toast.LENGTH_LONG).show();



        if (check == 1) {
            refChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    stringWaterLevel = dataSnapshot.getValue(String.class);
                    intWaterLevel = stringWaterLevel.replaceAll("[^0-9]", "");
                    waterLevel = Integer.parseInt(intWaterLevel);
                    tanksListtext.clear();
                    tanksListtextinch.clear();
//                    if(prev!=waterLevel)
//                    {noti=0;
//                    can=1;
//                    timerrrr();}
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
                    if(((d / y) * 100<20) && noti==0 && swt==1)
                { addNotification(tanks.get(i).getTitle());}
                    if((i+1==tanks.size()))
                    noti=1;}
                    prev = waterLevel;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            /** ....................................... **/


            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tanksListtext);
            listviewitems.setAdapter(arrayAdapter);
            refresh(1000); //refresh
            if (e == 1)
                e = 0;

        } else if (check == 0) {
            Toast.makeText(this, "waterlevel saved is " + prev, Toast.LENGTH_LONG).show();
            if (prev == null)//running the app for the first time, we will assume that the sensor is reading 0
            {
                prev = 0;
            }
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
                if(((d / y) * 100<20) && noti==0 && swt==1)
                { addNotification(tanks.get(i).getTitle());}

            }


            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tanksListtext);
            listviewitems.setAdapter(arrayAdapter);

            refresh(1000); //refresh
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

        if (activeInfo != null && activeInfo.isConnected()) { //connected with either mobile or wifi
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) { //wifi connected
                return 1;
            } else if (mobileConnected) { //mobile data connected

                return 1;
            }
        }

        return 0;
    }

    private void addNotification(String name) {

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",IMPORTANCE_HIGH);

                    // Configure the notification channel.
                    notificationChannel.setDescription("Channel description");

               //     notificationChannel.setLockscreenVisibility(1);

                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);

                }


                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

                notificationBuilder.setAutoCancel(true)
                        //.setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.alert)
                        .setTicker("Hearty365")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentTitle("Tank Level Notification")
                        .setContentText("The Level of water in "+name+" is  too low.")
                        .setContentInfo("Info")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                notificationManager.notify(/*notification id*/id, notificationBuilder.build());
                id++;

    }





            public void timerrrr() {


                if (swt == 1) {
                    CountDownTimer start = new CountDownTimer(ref, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            noti = 0;
                            id = 1;
                            timerrrr();
                        }


                    }.start();




                }
            }

    //refresh function
    private void refresh(int milliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadlistview();

            }
        };
        handler.postDelayed(runnable, milliseconds);

    }


}