
package com.example.database;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

//import static androidx.core.app.NotificationCompat.Builder;

public class MainActivity extends AppCompatActivity  {
    Integer imgid[] = {R.drawable.loft, R.drawable.sintex, R.drawable.loft};

    /**
     * added by Osama for cloud-app connection
     **/

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
//
 private FirebaseAuth firebaseAuth;


   final DatabaseReference refChild = databaseReference.child("users");

    String stringWaterLevel;
    String intWaterLevel;
    Integer waterLevel;
    static Integer prev;
    CountDownTimer countdowntimer;
        static int percent=0;


   static  int noti = 1;//notification default value is false
      static  int id=1;
     static   int swt=0;
static String name;
   static Editable notifi;
    static Editable percent1;
    static int ref=10000;//default value for notification recurrence
    private NotificationManager mNotifyManager;
    private Notification.Builder mBuilder;


    /**
     * .......................................
     **/

 //   protected ListView listviewitems;//listview to list all tanks

//    protected FloatingActionButton actionbutton;
    protected Button selectbutton;
    protected FloatingActionButton actionbutton2;
    protected TextView textView;
    ArrayList<String> tanksListtext = new ArrayList<>();//array to list tanks in cm's
    ArrayList<String> tanksListtextinch = new ArrayList<>();//array to list tanks in inches
    static int e = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       //April 7

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//DatabaseReference myRef = database.getReference();
//myRef.addValueEventListener(new ValueEventListener() {
// @Override
// public void onDataChange(DataSnapshot dataSnapshot) {
//    for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
//
//      Log.d("item id ",item_snapshot.child("item_id").getValue().toString());
//      Log.d("item desc",item_snapshot.child("item_desc").getValue().toString());
//     }
//  }
//}

        //April 7







        firebaseAuth = FirebaseAuth.getInstance();

        textView = findViewById(R.id.textView);
     //   listviewitems = findViewById(R.id.listviewitems);
//        actionbutton = findViewById(R.id.actionbutton);
        selectbutton = findViewById(R.id.selectbutton);
        actionbutton2 = findViewById(R.id.actionbutton2);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
//listing all tanks
        List<Tank> tanks = dbHelper.getAllTanks();




        selectbutton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);


                                                { openactivityselect();
                                                }


                                            }
        }
        );


        actionbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//once insert button is clicked it runs a new fragment called insert tank frag.

                Intent intent = new Intent(MainActivity.this, listtanks.class);

                startActivity(intent);
            }


        });

    //loadlistviews();

//        listviewitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//the selection from listview, once you click on any tank
//                // Intent intent = new Intent(MainActivity.this, tankview.class);
//                Intent intent = new Intent(MainActivity.this, tankview.class);//running the tankview activity
//
//                String tankselected = tanksListtext.get(position);//locating the position where it will pass the tank name and tank height
//                intent.putExtra("selectedd", tankselected);
//                startActivity(intent);
//
//
//            }
//        });






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


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference();






        switch (item.getItemId()) {

            case R.id.item1: {

                if (e == 1) {
                   item.setTitle("Convert from Metric to Imperial ");

                 e = 0;
                    return false;
                } else if (e == 0) { //whenever the value is 0, it means that its number and convert it to letters by calling the arraygrade that is inserted into arrayadapter to list the tanks in inches
                    item.setTitle("Convert from Imperial to Metric");

                    e = 1;//before exit set e to 1 so it will enter the If above condition
                    return false;
                }
                break;
            }

            case R.id.logout: {
                Logout();
                break;
            }
        }
            return false;
    }

    private void Logout(){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this, Login.class));
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


    public class CountDownTimerClass extends CountDownTimer {

        public CountDownTimerClass(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished/1000);

           // textview.setText(Integer.toString(progress));

        }

        @Override
        public void onFinish() {
//                         noti = 0;
//                            id = 1;
         //   textview.setText(" Count Down Finish ");
        //    countdowntimer.start();

        }

    }


    @Override
    public void onResume(){
        super.onResume();
        noti=1;




    }

//    public void startService(View view) {
//        startService(new Intent(this, service1.class));
//    }


    protected void onStart() {
        super.onStart();

SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profilefile), Context.MODE_PRIVATE);
    name = sharedPreferences.getString(getString(R.string.profilename), null);// retrieves the name field
     //   Toast.makeText(this, "thesavednameis"+name, Toast.LENGTH_SHORT).show();
        Log.v("Shared reading", "" + name);
    }



}