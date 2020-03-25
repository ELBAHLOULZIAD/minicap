package com.example.database;

import android.os.Bundle;
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

public class inserttankfrag extends DialogFragment {

  //  protected EditText coursetitle;
    protected EditText tanktitle;
   // protected EditText coursecode;
    protected EditText tankcode;
    protected Button savebutton;
    protected Button cancelbutton;

    protected RadioGroup radioGroup;
    @Nullable

int idradio=-1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tank, container, false);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        tanktitle = view.findViewById(R.id.tanktitle);
        tankcode = view.findViewById(R.id.tankcode);
        savebutton = view.findViewById(R.id.savebutton);
        cancelbutton = view.findViewById(R.id.cancelbutton);



        savebutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String title = tanktitle.getText().toString();
        String code = tankcode.getText().toString();
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
//                 ((MainActivity)getActivity()).tanksListtext.clear();//clears the array so it wont be repeated
//                ((MainActivity)getActivity()).tanksListtextinch.clear();
//            ((MainActivity)getActivity()).loadlistview();
                savebutton.setText("Added Successfully");
                savebutton.setEnabled(false);

            }
            else if(idradio==2)
            {

                dbhelper.insertTank(new Tank(title,code));
//                ((MainActivity)getActivity()).tanksListtext.clear();//clears the array so it wont be repeated
//                ((MainActivity)getActivity()).tanksListtextinch.clear();
//                ((MainActivity)getActivity()).loadlistview();
                savebutton.setText("Added Successfully");
                savebutton.setEnabled(false);
                //getDialog().dismiss();

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
                ((MainActivity)getActivity()).loadlistview();

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


    return view;
    }

}
