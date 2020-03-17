package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class selecttank extends AppCompatActivity {
    protected ListView list;
    String[] names={"    Reno","   Sintex","   Loft"};
    Integer imgid[]={R.drawable.reno,R.drawable.sintex,R.drawable.loft};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttank);

        list=findViewById(R.id.list);
        myadapter adapter = new myadapter();
        list.setAdapter(adapter);



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
            return view;
        }





        }

    }
