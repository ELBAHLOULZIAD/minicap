package com.example.database;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.database.R;

public class tankview extends AppCompatActivity {



    TextView tanktitle;
    TextView tankheight;

    String z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankview);

        tanktitle = (TextView) findViewById(R.id.tanktitle);
        tankheight = (TextView) findViewById(R.id.tankheight);

        String temptitle = getIntent().getStringExtra("selectedd");

        String[] stringsArray = temptitle.split("\n");
        tanktitle.setText(stringsArray[0]);
        tankheight.setText(stringsArray[1]);
        String level = stringsArray[2];

        String number  = level.replaceAll("[^0-9-.]", "");
        double  y = Double.parseDouble(String.valueOf(number));
        // Toast.makeText(getApplicationContext(), y, Toast.LENGTH_SHORT).show();
        ImageView container=(ImageView) findViewById(R.id.container);

        // new DecimalFormat("##.##").format((y));
        if (y>=70)
        {int imageResource=getResources().getIdentifier("@drawable/full",null,this.getPackageName());
            container.setImageResource(imageResource);
            Toast.makeText(getApplicationContext(), "The Tank is full", Toast.LENGTH_SHORT).show();}
        else if (y>=20 && y<=70)
        {int imageResource=getResources().getIdentifier("@drawable/half",null,this.getPackageName());
            container.setImageResource(imageResource);
            Toast.makeText(getApplicationContext(), "The Tank is half", Toast.LENGTH_SHORT).show();}
        else if (y<20)
        {int imageResource=getResources().getIdentifier("@drawable/empty",null,this.getPackageName());
            container.setImageResource(imageResource);
            Toast.makeText(getApplicationContext(), "The Tank is empty", Toast.LENGTH_SHORT).show();}
    };


}
