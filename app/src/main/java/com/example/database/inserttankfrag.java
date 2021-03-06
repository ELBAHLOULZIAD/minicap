package com.example.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    int n=-1;
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

        final String title = tanktitle.getText().toString();
        String code = tankcode.getText().toString();
        final String mac = macaddress.getText().toString();


      //  Toast.makeText(getActivity(), "the radio value before if " + idradio, Toast.LENGTH_SHORT).show();
        if (!(title.equals("") || code.equals("")|| code.equals("0"))) {


                if (idradio > 0) {
                    if (idradio == 1) {
                        final double m;
                        double r = Double.parseDouble(String.valueOf(code));
                        m = r * 2.14;
                        String tmpStr11 = String.valueOf(m);
                        // Toast.makeText(getActivity(), "the tank height in Centim is " + tmpStr11, Toast.LENGTH_SHORT).show();

                        final DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();

                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.child("users").child(MainActivity.name).hasChild(title)&&(mac.equals(""))) {
                                    Toast.makeText(getActivity(), "Tank is not added, Tank already Exists or Mac Address is missed", Toast.LENGTH_LONG).show();
                                } else {
                                    mDatabase.child("users").child((MainActivity.name)).child(title).child("Height").setValue(m);
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("Readings").setValue(0);
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("notification").setValue(idradio2);
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("macaddress").setValue(mac);
                                    String token = FirebaseInstanceId.getInstance().getToken();
                               //     Log.v("im reading", "" + MainActivity.name);
                                  //  Log.d("Refreshed token: ", token);
                                    // Toast.makeText(this, ""+token, Toast.LENGTH_LONG).show();
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("token").setValue(token);


                                    savebutton.setText("Added Successfully");
                                    savebutton.setEnabled(false);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        onPause();
                    }





            else if (idradio == 2) {

                        final double r = Double.parseDouble(String.valueOf(code));


                        final DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();

                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.child("users").child(MainActivity.name).hasChild(title)&&(mac.equals(""))) {
                                    Toast.makeText(getActivity(), "Tank is not added, Tank already Exists or Mac Address is missed", Toast.LENGTH_LONG).show();
                                } else {//mDatabase.child("users").child("Client_ID").child("Loft").child("Height").setValue(1000);
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("Height").setValue(r);
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("Readings").setValue(0);
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("notification").setValue(idradio2);
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("macaddress").setValue(mac);
                                    String token = FirebaseInstanceId.getInstance().getToken();

                                   // Log.d("Refreshed token: ", token);
                                    // Toast.makeText(this, ""+token, Toast.LENGTH_LONG).show();
                                    mDatabase.child("users").child(MainActivity.name).child(title).child("token").setValue(token);

                                    savebutton.setText("Added Successfully");
                                    savebutton.setEnabled(false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        onPause();
                    }


                }


            }

}

});
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




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
