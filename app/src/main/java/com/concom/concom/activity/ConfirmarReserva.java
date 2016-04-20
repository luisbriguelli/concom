package com.concom.concom.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.concom.concom.AutoCompleteConCom;
import com.concom.concom.R;
import com.concom.concom.TypeFaces;
import com.concom.concom.model.City;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfirmarReserva extends AppCompatActivity {

    TextView tvGuest, tvNote, tvDescription;
    String interestedPeople=null, guestCount=null;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmar_reserva);
        context = this;

        tvGuest= (TextView) findViewById(R.id.interestedGuest);
        tvNote = (TextView) findViewById(R.id.notes);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            interestedPeople = extras.getString("interestedPeople");
            guestCount = extras.getString("guestCount");
        }
        int c= Integer.parseInt(interestedPeople);
        c++;
        int g= Integer.parseInt(guestCount);
        g++;
        tvGuest.setText(c+"/"+g);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
