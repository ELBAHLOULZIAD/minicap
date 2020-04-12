package com.example.database;
//View the selected tank

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class tankview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

// private Spinner spinner;
    //private static final String[] paths = {"OFF", "Quarter","Half", "Three Quarters"};
    TextView tanktitle;
    TextView tankheight;
    protected Button deletebutton;
    protected Button button2;
    String z;
    String f;
String namef;
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


//        spinner = (Spinner) findViewById(R.id.spinner);
//        spinner.setOnItemSelectedListener(this);
//        List<String> list = new ArrayList<String>();







//
//        list.add("Three Quarter");
//        list.add("Half");
//        list.add("Quarter");
//        list.add("OFF");
////        else if (f.equals("Half"))
////        {list.add(f);
////            list.add("Quarter");
////            list.add("Three Quarter");
////            list.add("OFF");}
////        else if (f.equals("Three Quarter"))
////        {list.add(f);
////            list.add("Quarter");
////            list.add("Half");
////            list.add("OFF");}
////        else if (f.equals("OFF"))
////        {list.add(f);
////            list.add("Quarter");
////            list.add("Half");
////            list.add("Three Quarter");}
//
//
//
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);







    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "YOUR SELECTION IS SAVED : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
//
//        String temp=parent.getItemAtPosition(position).toString();
//        if(temp.equalsIgnoreCase("Quarter"))
//
//        { FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.getReference().child("users").child(MainActivity.name).child(namef).setValue(1);}
//
//    }
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
//                            DatabaseHelper dbHelper = new DatabaseHelper(tankview.this);
//                            final List<Tank> tanks = dbHelper.getAllTanks();
//                            Toast.makeText(getApplicationContext(), "The tank that will be deleted is " + tanktitle.getText(), Toast.LENGTH_LONG).show();
//                            for (int i = 0; i < tanks.size(); i++) {
//                                if (tanks.get(i).getTitle().equals(tanktitle.getText().toString())) {
//                                    dbHelper.deleteEntry(tanks.get(i).getID());
//                                    Toast.makeText(getApplicationContext(), "The tank is deleted successfully ", Toast.LENGTH_SHORT).show();
//                                }
//
//
//                            }
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child("users").child(MainActivity.name).child(tanktitle.getText().toString()).removeValue();

                            Toast.makeText(tankview.this, "The tank is deleted successfully", Toast.LENGTH_SHORT).show();
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




}

