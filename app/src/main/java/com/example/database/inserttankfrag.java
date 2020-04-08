package com.example.database;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.database.database.DatabaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class inserttankfrag extends DialogFragment {

  //  protected EditText coursetitle;
    protected EditText tanktitle;
   // protected EditText coursecode;
    protected EditText tankcode;
    protected EditText macaddress;
    protected Button savebutton;
    protected Button cancelbutton;

    protected RadioGroup radioGroup;
    protected RadioGroup radioGroup2;
    @Nullable

int idradio=-1;
    int idradio2=-1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tank, container, false);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        RadioGroup radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup2);
        tanktitle = view.findViewById(R.id.tanktitle);
        tankcode = view.findViewById(R.id.tankcode);
        macaddress=view.findViewById(R.id.macaddress);
        savebutton = view.findViewById(R.id.savebutton);
        cancelbutton = view.findViewById(R.id.cancelbutton);



        savebutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String title = tanktitle.getText().toString();
        String code = tankcode.getText().toString();
        String mac = macaddress.getText().toString();
        DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
      //  Toast.makeText(getActivity(), "the radio value before if " + idradio, Toast.LENGTH_SHORT).show();
        if (!(title.equals("") || code.equals("")|| code.equals("0")))
        {

            { if(idradio>0)
            { if (idradio==1)
            {double m;
            double r = Double.parseDouble(String.valueOf(code));
                m=r*2.14;
                String tmpStr11 = String.valueOf(m);
               // Toast.makeText(getActivity(), "the tank height in Centim is " + tmpStr11, Toast.LENGTH_SHORT).show();
                dbhelper.insertTank(new Tank(title,tmpStr11));
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                //mDatabase.child("users").child("Client_ID").child("Loft").child("Height").setValue(1000);
                mDatabase.child("users").child("40036145").child(title).child("Height").setValue(m);
                mDatabase.child("users").child("40036145").child(title).child("Readings").setValue(0);
                mDatabase.child("users").child("40036145").child(title).child("notification").setValue(idradio2);
                mDatabase.child("users").child("40036145").child(title).child("macaddress").setValue(mac);
                String token = FirebaseInstanceId.getInstance().getToken();

                Log.d("Refreshed token: ", token);
               // Toast.makeText(this, ""+token, Toast.LENGTH_LONG).show();
                mDatabase.child("users").child("40036145").child(title).child("token").setValue(token);



//                 ((MainActivity)getActivity()).tanksListtext.clear();//clears the array so it wont be repeated
//                ((MainActivity)getActivity()).tanksListtextinch.clear();
//            ((MainActivity)getActivity()).loadlistview();
                savebutton.setText("Added Successfully");
                savebutton.setEnabled(false);
            onPause();
            }
            else if(idradio==2)
            {
                double r = Double.parseDouble(String.valueOf(code));
                dbhelper.insertTank(new Tank(title,code));
//                ((MainActivity)getActivity()).tanksListtext.clear();//clears the array so it wont be repeated
//                ((MainActivity)getActivity()).tanksListtextinch.clear();
//                ((MainActivity)getActivity()).loadlistview();

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                //mDatabase.child("users").child("Client_ID").child("Loft").child("Height").setValue(1000);
                mDatabase.child("users").child("40036145").child(title).child("Height").setValue(r);
                mDatabase.child("users").child("40036145").child(title).child("Readings").setValue(0);
                mDatabase.child("users").child("40036145").child(title).child("notification").setValue(idradio2);
                mDatabase.child("users").child("40036145").child(title).child("macaddress").setValue(mac);
                String token = FirebaseInstanceId.getInstance().getToken();

                Log.d("Refreshed token: ", token);
                // Toast.makeText(this, ""+token, Toast.LENGTH_LONG).show();
                mDatabase.child("users").child("40036145").child(title).child("token").setValue(token);

                savebutton.setText("Added Successfully");
                savebutton.setEnabled(false);
                //getDialog().dismiss();
                onPause();
            }
       }}
        }
}

});
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                                ((MainActivity)getActivity()).tanksListtext.clear();//clears the array so it wont be repeated
//                ((MainActivity)getActivity()).tanksListtextinch.clear();
           //     ((MainActivity)getActivity()).loadlistview();

                getDialog().dismiss();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch(checkedId) {
                    case R.id.radio_one:

                        idradio=1;
                        break;
                    case R.id.radio_two:
                        // Fragment 2
                        idradio=2;
                        break;
                }
              //  Toast.makeText(getActivity(), "Selected Radio Button: " + idradio, Toast.LENGTH_SHORT).show();
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch(checkedId) {
                    case R.id.quarter:

                        idradio2=1;
                        break;
                    case R.id.half:
                        // Fragment 2
                        idradio2=2;
                        break;

                    case R.id.tquarter:                        // Fragment 2
                        idradio2=3;
                        break;
                    case R.id.nonot:                        // Fragment 2
                        idradio2=4;
                        break;

                }
                //  Toast.makeText(getActivity(), "Selected Radio Button: " + idradio, Toast.LENGTH_SHORT).show();
            }
        });
    return view;
    }

}
