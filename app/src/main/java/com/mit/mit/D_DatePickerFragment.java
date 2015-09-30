package com.mit.mit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by baudraim on 29/09/2015.
 */

public class D_DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String cas;


    public D_DatePickerFragment(String cas)
    {
        this.cas=cas;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        //TextView activityButton = (TextView)getActivity().findViewById(R.id.newProject_lb_date);
        //activityButton.setText (day+"/"+month+"/"+year);

        if(cas.equals("newproject_debut"))
        {
            int mois = month+1;
            EditText activityObject = (EditText)getActivity().findViewById(R.id.newProject_tb_debut);
            activityObject.setText (day+"/"+mois+"/"+year);
        }

        if(cas.equals("newproject_fin"))
        {
            int mois = month+1;
            EditText activityObject = (EditText)getActivity().findViewById(R.id.newProject_tb_fin);
            activityObject.setText (day+"/"+mois+"/"+year);
        }
    }


}