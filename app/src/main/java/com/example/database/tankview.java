package com.example.database;
//View the selected tank

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class tankview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

 private Spinner spinner;
    //private static final String[] paths = {"OFF", "Quarter","Half", "Three Quarters"};
    TextView tanktitle;
    TextView tankheight;
    protected Button deletebutton;
    protected Button button2;
    String z;
    String f;
String namef;
String name;

    static int value=0;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_tankview);
        deletebutton = findViewById(R.id.deletebutton);
        button2 = findViewById(R.id.button2);
        tanktitle = (TextView) findViewById(R.id.tanktitle);
        tankheight = (TextView) findViewById(R.id.tankheight);

        String temptitle = getIntent().getStringExtra("selectedd");

        final String[] stringsArray = temptitle.split("\n");
        tanktitle.setText(stringsArray[0]);
        tankheight.setText(stringsArray[1]);
        String[] nato=stringsArray[3].split(":");
//        value = Integer.parseInt(nato[1]);
        namef=stringsArray[0];
        String level = stringsArray[2];
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//once insert button is clicked it runs a new fragment called insert tank frag.

                deletetank();

            }


        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//once insert button is clicked it runs a new fragment called insert tank frag.

                finish();

            }


        });
        String number = level.replaceAll("[^0-9-.]", "");
        double y = Double.parseDouble(String.valueOf(number));
        // Toast.makeText(getApplicationContext(), y, Toast.LENGTH_SHORT).show();
        ImageView container = (ImageView) findViewById(R.id.container);

        // new DecimalFormat("##.##").format((y));
        if (y >= 70) {
            int imageResource = getResources().getIdentifier("@drawable/full", null, this.getPackageName());
            container.setImageResource(imageResource);
            Toast.makeText(getApplicationContext(), "The Tank is full", Toast.LENGTH_SHORT).show();
        } else if (y >= 20 && y <= 70) {
            int imageResource = getResources().getIdentifier("@drawable/half", null, this.getPackageName());
            container.setImageResource(imageResource);
            Toast.makeText(getApplicationContext(), "The Tank is half", Toast.LENGTH_SHORT).show();
        } else if (y < 20) {
            int imageResource = getResources().getIdentifier("@drawable/empty", null, this.getPackageName());
            container.setImageResource(imageResource);
            Toast.makeText(getApplicationContext(), "The Tank is empty", Toast.LENGTH_SHORT).show();
        }


        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        final List<String> list = new ArrayList<String>();




//        rootRef.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                String test=snapshot.child("users").child(name).child(namef).child("notification").getValue().toString();
//                value = Integer.parseInt(test);
//             //   Toast.makeText(g, "The value of V is"+value, Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



        if (nato[1].equalsIgnoreCase("Three Quarter"))
        {list.add("Three Quarter");
            list.add("Half");
            list.add("Quarter");
            list.add("OFF");}
        if (nato[1].equalsIgnoreCase("Half"))
        {
            list.add("Half");
            list.add("Three Quarter");
            list.add("Quarter");
            list.add("OFF");}
        if (nato[1].equalsIgnoreCase("Quarter"))
        {list.add("Quarter");
            list.add("Half");
            list.add("Three Quarter");
            list.add("OFF");}


        if (nato[1].equalsIgnoreCase("OFF"))
        {list.add("OFF");;
            list.add("Quarter");
            list.add("Half");
            list.add("Three Quarter");
            ;}
//
        Toast.makeText(this, "The value of V is"+nato[1], Toast.LENGTH_LONG).show();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);







    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "YOUR SELECTION IS SAVED : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

        String temp=parent.getItemAtPosition(position).toString();
        if(temp.equalsIgnoreCase("Quarter"))

        { FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("users").child(name).child(namef).child("notification").setValue(1);}

        if(temp.equalsIgnoreCase("Three Quarter"))

        { FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("users").child(name).child(namef).child("notification").setValue(3);}
        if(temp.equalsIgnoreCase("OFF"))

        { FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("users").child(name).child(namef).child("notification").setValue(4);}
        if(temp.equalsIgnoreCase("Half"))

        { FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("users").child(name).child(namef).child("notification").setValue(2);}

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }
    ;

    public void deletetank() {



                    AlertDialog.Builder builder = new AlertDialog.Builder((this));
                    // DatabaseHelper dbHelper = new DatabaseHelper(this);
                    builder.setMessage("Are you sure you want to delete the tank").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child("users").child(name).child(tanktitle.getText().toString()).removeValue();

                            Toast.makeText(tankview.this, "The tank is deleted successfully", Toast.LENGTH_LONG).show();
                            onPause();
                      finish();  }})
                            .setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                                public void onClick(DialogInterface dialogInterface, int which){
                            dialogInterface.cancel();
                        }
                    });
                AlertDialog alertDialog= builder.create();
                    alertDialog.show();




    }
    protected void onStart() {
        super.onStart();


        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profilefile), Context.MODE_PRIVATE);
        name = sharedPreferences.getString(getString(R.string.profilename), null);// retrieves the name field


    }



}

