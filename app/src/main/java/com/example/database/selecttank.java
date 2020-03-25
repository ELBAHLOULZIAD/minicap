package com.example.database;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class selecttank extends AppCompatActivity {
    protected ListView list;
    protected FloatingActionButton actionbutton;
    String[] names={"Name: Reno\nCapacity: 2000 Liters\nHeight: 62cm ","Name: Sintex\nCapacity: 1000 Liters\nHeight: 150cm ","Name: Loft\nCapacity: 4163 Liters\nHeight: 210cm"};
    Integer imgid[]={R.drawable.reno,R.drawable.sintex,R.drawable.loft};
    ArrayList<String> tanklist = new ArrayList<>();
static int id1=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionbutton = findViewById(R.id.actionbutton);

        setContentView(R.layout.activity_selecttank);

        list=findViewById(R.id.list);
        myadapter adapter = new myadapter();
        list.setAdapter(adapter);




//        actionbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {//once insert button is clicked it runs a new fragment called insert tank frag.
//                DatabaseHelper dbHelper = new DatabaseHelper(selecttank.this);
//
//                List<Tank> tanks = dbHelper.getAllTanks();
//                if(tanks.size()<1)
//                {inserttankfrag dialog = new inserttankfrag();
//                    dialog.show(getSupportFragmentManager(), "Insert Tank");
//                }
//                else
//                    Toast.makeText(selecttank.this, " The app works for one tank at the right time " , Toast.LENGTH_LONG).show();
//
//            }
//
//
//        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tankselected = tanklist.get(position);//locating the position where it will pass the tank name and tank height
                final String[] stringsArray = tankselected.split("\n");

                final String f= stringsArray[2].replaceAll("[^0-9]", "");
                final String[] b = stringsArray[0].split(":");
                AlertDialog.Builder builder = new AlertDialog.Builder((selecttank.this));
                // DatabaseHelper dbHelper = new DatabaseHelper(this);
                builder.setMessage("Are you sure you want to add "+b[1]+" tank").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(selecttank.this);
                        dbHelper.insertTank(new Tank(b[1],f));


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
        });






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//creates the drop down menu with the item for conversion
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DatabaseHelper dbHelper = new DatabaseHelper(selecttank.this);

                List<Tank> tanks = dbHelper.getAllTanks();
                if(tanks.size()<1)
                {inserttankfrag dialog = new inserttankfrag();
                    dialog.show(getSupportFragmentManager(), "Insert Tank");
                }
                else
                    Toast.makeText(selecttank.this, " The app works for one tank at the right time " , Toast.LENGTH_LONG).show();



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




    }
