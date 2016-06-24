package com.example.dbirulia.simpletodoapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbirulia.simpletodoapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity {

    int pos;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("details");
        this.pos = getIntent().getIntExtra("pos", 0);
        EditText editTextItem = (EditText)findViewById(R.id.etItem);
        editTextItem.setText(name);
        EditText detailsTextItem = (EditText)findViewById(R.id.etDetails);
        detailsTextItem.setText(description);
        priority = getIntent().getStringExtra("priority");
        if (priority.equals("")){
           priority = "high";
        }
        setPriority(priority);


        Integer duedate = getIntent().getIntExtra("duedate", 0);
        if (duedate != 0){
            Date dd = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            try{
                dd = sdf.parse(duedate.toString());
            }catch(ParseException ex){
                // log exception
            }
            calendar = Calendar.getInstance();
            calendar.setTime(dd);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        else {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            day++;
        }
        showDate(year, month, day);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    public void showDate(int year, int month, int day){
        TextView tvDate = (TextView)findViewById(R.id.tvDate);
        String sd = month + "/" + day + "/" + year;
        tvDate.setText(sd);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, dateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            year = arg1;
            month = arg2;
            day = arg3;
            showDate(year, month, day);
        }
    };

    public void onSaveItem(View v){
        EditText editTextItem = (EditText) findViewById(R.id.etItem);
        EditText detailsTextItem = (EditText) findViewById(R.id.etDetails);
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        String dateStr = tvDate.getText().toString();
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("name", editTextItem.getText().toString());
        data.putExtra("details", detailsTextItem.getText().toString());
        data.putExtra("priority", priority);
        data.putExtra("pos", this.pos);
        data.putExtra("duedate", dateToInteger());
        data.putExtra("code", 200);
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    public void onCancel(View v){
        finish(); // closes the activity, pass data to parent
    }

    public void onRadioButtonClicked(View v){

        boolean checked = ((RadioButton) v).isChecked();

        switch(v.getId()) {
            case R.id.radio_high:
                if (checked)
                    priority = "high";
                    break;
            case R.id.radio_medium:
                if (checked)
                    priority = "medium";
                    break;
            case R.id.radio_low:
                if (checked)
                    priority = "low";
                break;
        }
    }
    public void setPriority(String priority){
        switch(priority) {
            case "high":
                RadioButton rbHigh = (RadioButton)findViewById(R.id.radio_high);
                rbHigh.toggle();
                break;
            case "medium":
                RadioButton rbMedium = (RadioButton)findViewById(R.id.radio_medium);
                rbMedium.toggle();
                break;
            case "low":
                RadioButton rbLow = (RadioButton)findViewById(R.id.radio_low);
                rbLow.toggle();
                break;
        }
    }


    private Integer dateToInteger(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date dd = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMdd");
        Integer intDate = Integer.parseInt(dateFormat.format(dd));
        return intDate;
    }

}

