
package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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



    /** added by Osama for cloud-app connection **/

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    final DatabaseReference refChild = databaseReference.child("Test").child("Stream").child("String");

    String stringWaterLevel;
    String intWaterLevel;
    Integer waterLevel;
    Integer prev;

    /** ....................................... **/

    protected ListView listviewitems;

    protected FloatingActionButton actionbutton;
    ArrayList<String> tanksListtext=new ArrayList<>();
    ArrayList<String> tanksListtextinch=new ArrayList<>();
    int e=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listviewitems = findViewById(R.id.listviewitems);
        actionbutton = findViewById(R.id.actionbutton);


        actionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inserttankfrag dialog = new inserttankfrag();
                dialog.show(getSupportFragmentManager(), "Insert Tank");
            }


        });

        loadlistview();
        listviewitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Intent intent = new Intent(MainActivity.this, tankview.class);
                Intent intent = new Intent(MainActivity.this, tankview.class);

                String tankselected=tanksListtext.get(position);
                intent.putExtra("selectedd",tankselected);
                startActivity(intent);


            }
        });


    }

    ;

    public boolean onCreateOptionsMenu(Menu menu) {//creates the drop down menu with the item grade convertor
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item1) {
        if (item1.isEnabled()& e==1) { //whenever the value is 1, it means that its number and convert it to letters by calling the arraygrade that is inserted into arrayadapter


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tanksListtext);
            String list;
            listviewitems.setAdapter(arrayAdapter);
            e=0;//before returning set e=0 so the next time it will alternate to the numeric array
            return false;
        }
        else if (item1.isEnabled()& e==0){ //whenever the value is 0, it means that its number and convert it to letters by calling the arraygrade that is inserted into arrayadapter

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tanksListtextinch);
            listviewitems.setAdapter(arrayAdapter);
            e=1;//before exit set e to 1 so it will enter the If above condition
            return false;
             }return false;
    }
    protected void loadlistview()
        {
            DatabaseHelper dbHelper =new DatabaseHelper(this);
            //List<Course> courses =dbHelper.getAllCourse();
            final List<Tank> tanks =dbHelper.getAllTanks();


            /** added by Osama for cloud-app connection **/

            refChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    stringWaterLevel = dataSnapshot.getValue(String.class);
                    intWaterLevel = stringWaterLevel.replaceAll("[^0-9]","");
                    waterLevel = Integer.parseInt(intWaterLevel);
                    tanksListtext.clear();
                    tanksListtextinch.clear();
                    for (int i=0;i<tanks.size();i++)
                    {
                        String temp="";
                        String temp2="";
                        temp+=tanks.get(i).getTitle()+"\n";


                        temp2+=tanks.get(i).getTitle()+"\n";

                        double y = Double.parseDouble(String.valueOf(tanks.get(i).getCode()));
                        double d = ((y-waterLevel)); // added by Osama for cloud-app connection
                        double z=y*0.393701;
                        temp+="The height is: "+new DecimalFormat("##.##").format(y)+"cm\n";
                        temp2+="The height is: "+new DecimalFormat("##.##").format(z)+"inches\n";
                       // temp+="The height is: "+courses.get(i).getCode()+"\n";
                        temp+="The level of water available is: "+ new DecimalFormat("##.##").format((d/y)*100)+"%";
                        temp2+="The level of water available is: "+ new DecimalFormat("##.##").format((d/y)*100)+"%";
                        tanksListtext.add(temp);
                        tanksListtextinch.add(temp2);

                    }
               }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            /** ....................................... **/





                 ArrayAdapter arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tanksListtext);
                 listviewitems.setAdapter(arrayAdapter);
            refresh(1000); }
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
