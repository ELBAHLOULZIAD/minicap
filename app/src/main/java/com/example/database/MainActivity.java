
package com.example.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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




//   static  int noti = 1;//notification default value is false
//      static  int id=1;
//     static   int swt=0;
static String name;
//   static Editable notifi;
   // static Editable percent1;
   // static int ref=10000;//default value for notification recurrence
//    private NotificationManager mNotifyManager;
//    private Notification.Builder mBuilder;


    /**
     * .......................................
     **/

 //   protected ListView listviewitems;//listview to list all tanks

//    protected FloatingActionButton actionbutton;
    protected Button selectbutton;
    protected FloatingActionButton actionbutton2;
    protected TextView textView;

    static int e = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);










        firebaseAuth = FirebaseAuth.getInstance();

        textView = findViewById(R.id.textView);

        selectbutton = findViewById(R.id.selectbutton);
        actionbutton2 = findViewById(R.id.actionbutton2);






        selectbutton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


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


//    public class CountDownTimerClass extends CountDownTimer {
//
//        public CountDownTimerClass(long millisInFuture, long countDownInterval) {
//
//            super(millisInFuture, countDownInterval);
//
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//            int progress = (int) (millisUntilFinished/1000);
//
//           // textview.setText(Integer.toString(progress));
//
//        }
//
//        @Override
//        public void onFinish() {
////                         noti = 0;
////                            id = 1;
//         //   textview.setText(" Count Down Finish ");
//        //    countdowntimer.start();
//
//        }
//
//    }






    protected void onStart() {
        super.onStart();
        int t = isOnline();
        if (t == 0) {
            Toast.makeText(this, "Kindly check your internet connection, it is unstable", Toast.LENGTH_SHORT).show();
            selectbutton.setEnabled(false);
            actionbutton2.setEnabled(false);

        }

        if (t == 1)
        {selectbutton.setEnabled(true);
            actionbutton2.setEnabled(true);}
SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profilefile), Context.MODE_PRIVATE);
    name = sharedPreferences.getString(getString(R.string.profilename), null);// retrieves the name field
     //   Toast.makeText(this, "thesavednameis"+name, Toast.LENGTH_SHORT).show();
        Log.v("Shared reading", "" + name);
    }


    public void open(View view){
         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.accuweather.com/en/world-weather"));
         startActivity(browserIntent);
    }

}