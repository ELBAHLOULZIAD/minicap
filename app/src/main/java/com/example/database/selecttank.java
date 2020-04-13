package com.example.database;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class selecttank extends AppCompatActivity {
    protected ListView list;
    protected FloatingActionButton actionbutton;
    String[] names={"Name:Reno\nCapacity:2000Liters\nHeight:62cm","Name:Sintex\nCapacity:1000Liters\nHeight:150cm","Name:Loft\nCapacity:4163Liters\nHeight:210cm"};
    Integer imgid[]={R.drawable.reno,R.drawable.sintex,R.drawable.loft};
    ArrayList<String> tanklist = new ArrayList<>();
    int selected;
static String name;
static int id1=1;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        actionbutton = findViewById(R.id.actionbutton);

        setContentView(R.layout.activity_selecttank);
        final String token = FirebaseInstanceId.getInstance().getToken();
        list=findViewById(R.id.list);
        myadapter adapter = new myadapter();
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tankselected = tanklist.get(position);//locating the position where it will pass the tank name and tank height
                final String[] stringsArray = tankselected.split("\n");

                final String f= stringsArray[2].replaceAll("[^0-9]", "");
                final String[] b = stringsArray[0].split(":");
               // AlertDialog.Builder builder = new AlertDialog.Builder((selecttank.this)).setTitle("Sensor MC").setMessage("Enter MCAd");

                // DatabaseHelper dbHelper = new DatabaseHelper(this);
                //DatabaseHelper dbHelper = new DatabaseHelper(selecttank.this);
              //  List<Tank> tanks = dbHelper.getAllTanks();
                final EditText input = new EditText(selecttank.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                final String[] items = {"Quarter","Half","3/4","OFF"};
                int checkedItem = 3;

                //AlertDialog.setIcon(R.drawable.key);
                AlertDialog.Builder builder = new AlertDialog.Builder((selecttank.this)).setView(input).setTitle("Please Enter MacAddress and Set Notification").setSingleChoiceItems(items,checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(ListAlertDialogActivity.this, "Position: " + which + " Value: " + listItems[which], Toast.LENGTH_LONG).show();
                                if(items[which]=="Quarter")
                                {selected =1;}
                                if(items[which]=="Half")
                                {selected =2;}
                                if(items[which]=="3/4")
                                {selected =3;}
                                if(items[which]=="OFF")
                                {selected =4;}

                            }
                        });

                builder//.setMessage("Are you sure you want to add " + b[1] + " tank")
                            .setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(!input.getText().toString().equals("")) {
//                                DatabaseHelper dbHelper = new DatabaseHelper(selecttank.this);
//                                dbHelper.insertTank(new Tank(b[1], f));

                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                rootRef.child("users").child(name);
                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (!snapshot.child("users").child(name).hasChild(b[1])) {
                                            DatabaseReference mDatabase;
                                            mDatabase = FirebaseDatabase.getInstance().getReference();
                                            double r = Double.parseDouble(f);
                                            //mDatabase.child("users").child("Client_ID").child("Loft").child("Height").setValue(1000);
                                            mDatabase.child("users").child(name).child(b[1]).child("Height").setValue(r);
                                            mDatabase.child("users").child(name).child(b[1]).child("Readings").setValue(0);
                                            mDatabase.child("users").child(name).child(b[1]).child("notification").setValue(selected);
                                            mDatabase.child("users").child(name).child(b[1]).child("macaddress").setValue(input.getText().toString());
                                            mDatabase.child("users").child(name).child(b[1]).child("token").setValue(token);
                                            Toast.makeText(selecttank.this, "The tank is added successfully", Toast.LENGTH_LONG).show();


                                        }
                                        else
                                            Toast.makeText(selecttank.this, "Tank already Exists", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });




         /////////////////////////////////////
//                                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot snapshot) {
//                                        if (snapshot.child("users").child(name).hasChild(b[1])) {
//                                            Toast.makeText(selecttank.this, "Tank already Exists", Toast.LENGTH_SHORT).show();
//                                        } else {
//
////Writing Hashmap
//
//                                            DatabaseReference mDatabase;
//                                            mDatabase = FirebaseDatabase.getInstance().getReference();
//                                            double r = Double.parseDouble(f);
//                                            //mDatabase.child("users").child("Client_ID").child("Loft").child("Height").setValue(1000);
//                                            mDatabase.child("users").child(name).child(b[1]).child("Height").setValue(r);
//                                            mDatabase.child("users").child(name).child(b[1]).child("Readings").setValue(0);
//                                            mDatabase.child("users").child(name).child(b[1]).child("notification").setValue(selected);
//                                            mDatabase.child("users").child(name).child(b[1]).child("macaddress").setValue(input.getText().toString());
//
//
//                                         //   Log.d("Refreshed token: ", token);
//                                            // Toast.makeText(this, ""+token, Toast.LENGTH_LONG).show();
//                                            mDatabase.child("users").child(name).child(b[1]).child("token").setValue(token);
//                                            Toast.makeText(selecttank.this, "The tank is added successfully", Toast.LENGTH_LONG).show();
//
//                                        }
//
//                                    }
//                                        @Override
//                                        public void onCancelled (@NonNull DatabaseError
//                                        databaseError){
//
//                                        }
//
//                                    });





/////////////////////////////




                            }







                            else
                            {
                                Toast.makeText(selecttank.this, "Please fill the Sensor McAddress", Toast.LENGTH_LONG).show();
                            }


                        }

                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();






            }
        });






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//creates the drop down menu with the item for conversion
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



                inserttankfrag dialog = new inserttankfrag();
                    dialog.show(getSupportFragmentManager(), "Insert Tank");




        return false;
    }


    class myadapter extends BaseAdapter {
        @Override
        public int getCount(){
        return imgid.length;}
        @Override
        public Object getItem(int Position){
            return null;
        }
        @Override
        public long getItemId(int Position)
        {return 0;}

        @Override

        public View getView(int position, View convertView, ViewGroup parent){

            View view=getLayoutInflater().inflate(R.layout.activity_selecttank,null);
            ImageView mImageView=(ImageView) view.findViewById(R.id.imageView);
            TextView mTextView=(TextView) view.findViewById(R.id.textVew);
            mImageView.setImageResource(imgid[position]);
            mTextView.setText(names[position]);
            tanklist.add(names[position]);
            return view;
        }





        }




    protected void onStart() {
        super.onStart();


        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profilefile), Context.MODE_PRIVATE);
        name = sharedPreferences.getString(getString(R.string.profilename), null);// retrieves the name field


    }








    }
