package com.example.database;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class confnoti extends DialogFragment {
    protected TextView configtext;
    protected EditText timetext;
    protected Button savebutton;
    protected Button cancelbutton;
    @Nullable
    protected Switch switch2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_confnoti, container, false);
        final Switch switch2 = (Switch) view.findViewById(R.id.switch2);

        switch2.setOnCheckedChangeListener(new
                                                   CompoundButton.OnCheckedChangeListener() {
                                                       @Override
                                                       public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                           if (b) {configtext.setEnabled(true);
                                                               timetext.setEnabled(true);
                                                               savebutton.setEnabled(true);

                                                           }
                                                           else {configtext.setEnabled(false);
                                                               timetext.setEnabled(false);
                                                               savebutton.setEnabled(true);

                                                           }
                                                       }
                                                   });
                configtext = view.findViewById(R.id.configtext);
        timetext = view.findViewById(R.id.timetext);
        savebutton = view.findViewById(R.id.savebutton);
        cancelbutton = view.findViewById(R.id.cancelbutton);
        configtext.setEnabled(false);
        timetext.setEnabled(false);
        savebutton.setEnabled(false);

       if(((MainActivity)getActivity()).swt==1) {
           switch2.setChecked(true);
           timetext.setText(((MainActivity)getActivity()).notifi);
       }


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (switch2.isChecked()) {
                        Editable x = timetext.getText();
                        ((MainActivity) getActivity()).notifi = x;
                        String time = x.toString();
                        String[] units = time.split(":"); //will break the string up into an array
                        int hours = Integer.parseInt(units[0]); //first element
                        int minutes = Integer.parseInt(units[1]); //second element
                        int seconds = Integer.parseInt(units[2]);
                        int duration = (60 * minutes) + (3600 * hours) + seconds; //add up our values

                    if(hours<=24&minutes<=60&seconds<=60&duration>0) {
                        ((MainActivity) getActivity()).swt = 1;
                        ((MainActivity) getActivity()).noti = 0;
                        ((MainActivity) getActivity()).ref = duration*1000;
                        configtext.setEnabled(false);
                        timetext.setEnabled(false);
                        savebutton.setEnabled(false);
                        ((MainActivity) getActivity()).id= 1;
                        ((MainActivity)getActivity()).countdowntimer.cancel();
                        ((MainActivity)getActivity()).countdowntimer.start();
                        savebutton.setText("New time is set");}

                    else
                        {Toast.makeText(getActivity(), "Please Enter the time hh:mm:ss " ,Toast.LENGTH_LONG).show();}


                    }
                    else if (!switch2.isChecked()){

                        ((MainActivity) getActivity()).swt = 0;
                        ((MainActivity) getActivity()).ref = 0;
                        savebutton.setEnabled(false);
                        savebutton.setText("Notification is Canceled");
                    }
                }
                        catch(Exception ex){
                            Toast.makeText(getActivity(), "Please Enter the time hh:mm:ss " ,Toast.LENGTH_LONG).show();
            }}
        });





        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });



   return view; };
}



