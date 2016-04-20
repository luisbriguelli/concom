package com.concom.concom.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText checkIn,checkOut;
    SeekBar seekBarPeople, seekBarPrice;
    TextView tvSeekBarPeople, tvSeekBarPrice, appName, tvPriceNote;
    AutoCompleteConCom localName;
    List<City> cities;
    ArrayAdapter<String> adapter;
    List<String> names = new ArrayList<>();
    List<String> codes = new ArrayList<>();
    int progressValuePrice = 0;
    int guestCount = 1;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        appName= (TextView) findViewById(R.id.textView_app_name);
        localName= (AutoCompleteConCom) findViewById(R.id.editTextLocal);
        checkIn= (EditText) findViewById(R.id.editTextCheckIn);
        checkOut= (EditText) findViewById(R.id.editTextCheckOut);
        tvSeekBarPeople = (TextView) findViewById(R.id.textViewSeekBarPeople);
        seekBarPeople = (SeekBar) findViewById(R.id.seekBarPeople);
        tvSeekBarPrice = (TextView) findViewById(R.id.textViewSeekBarPrice);
        tvPriceNote = (TextView) findViewById(R.id.textViewPriceNote);
        seekBarPrice = (SeekBar) findViewById(R.id.seekBarPrice);

        appName.setTypeface(TypeFaces.getTypeFace(this, "Roboto_Thin.ttf"));
        tvSeekBarPrice.setTypeface(TypeFaces.getTypeFace(this, "Roboto_Thin.ttf"));
        tvSeekBarPeople.setTypeface(TypeFaces.getTypeFace(this, "Roboto_Thin.ttf"));
        tvPriceNote.setTypeface(TypeFaces.getTypeFace(this, "Roboto_Thin.ttf"));

        cities = new Select().from(City.class).execute();
        for(City city:cities){
            names.add(city.getName());
            codes.add(city.getCode());
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        localName.setAdapter(adapter);

        seekBarPeople.setMax(8);
        seekBarPeople.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                guestCount = progress;
                final int MIN = 1;
                if (guestCount <= MIN) {
                    tvSeekBarPeople.setText(MIN + " colega de quarto");
                    seekBar.setProgress(MIN);
                } else {
                    tvSeekBarPeople.setText(guestCount + " colegas de quarto");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarPrice.setMax(3001);
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValuePrice = (Math.round(progress/20 ))*20;
                final int MIN = 0;
                if(progress==MIN){
                    tvSeekBarPrice.setText("R$ "+MIN + ",00*");
                }else if (progressValuePrice == seekBar.getMax()) {
                    tvSeekBarPrice.setText("+R$ "+progressValuePrice + ",00*");
                } else {
                    tvSeekBarPrice.setText("R$ "+progressValuePrice + ",00*");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        checkIn.setFocusable(false);
        checkOut.setFocusable(false);
        checkIn.setClickable(true);
        checkOut.setClickable(true);
        /*ObjectMapper mapper = new ObjectMapper();
        ActiveAndroid.beginTransaction();
        try {
            JSONArray j = new JSONArray(loadJSONFromAsset());
            JSONObject jsonObject;
            for(int i =0;i<j.length();i++) {
                jsonObject = j.getJSONObject(i);
                jsonObject.length();
                City city = mapper.readValue(jsonObject.toString(), City.class);
                if(city!=null){
                    city.save();
                }
            }
            ActiveAndroid.setTransactionSuccessful();

        } catch (JSONException e) {
            Log.e("a",e.toString());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            Log.e("a",e.toString());

            e.printStackTrace();
        } catch (JsonParseException e) {
            Log.e("a",e.toString());

            e.printStackTrace();
        } catch (IOException e) {
            Log.e("a",e.toString());

            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }


        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        if (sd.canWrite()) {
            File backupDB = new File(sd, "hack.db");
            File currentDB = this.getApplicationContext().getDatabasePath("concom.db"); //databaseName=your current application database name, for example "my_data.db"
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("airports.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            json=json.replace("\n","");
            int i = json.indexOf("\n");
            int id = json.lastIndexOf("\n");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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

    public void onClickCheckIn(View view) {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        CalendarDay day = CalendarDay.today();
        showDatePickerDialog(this, day, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
                int mes = monthOfYear+1;
                String date2 =  dayOfMonth+ "/" +mes +"/" +year1;
                checkIn.setText(date2);
                String date = checkIn.getText().toString();
                int day = Integer.parseInt(date.substring(0, date.indexOf("/")));
                int month = Integer.parseInt(date.substring(date.indexOf("/")+1, date.lastIndexOf("/")));
                int year = Integer.parseInt(date.substring(date.lastIndexOf("/")+1,date.length()));
                month--;
                day++;
                CalendarDay dayy = CalendarDay.from(year, month,day);
                showDatePickerDialog(context, dayy, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int mes = monthOfYear + 1;
                        String date = dayOfMonth + "/" + mes + "/" + year;
                        checkOut.setText(date);
                    }
                });
            }
        });
    }

    public void onClickCheckOut(View view) {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(!checkIn.getText().toString().isEmpty()){
            String date = checkIn.getText().toString();
            int day = Integer.parseInt(date.substring(0, date.indexOf("/")));
            int month = Integer.parseInt(date.substring(date.indexOf("/")+1, date.lastIndexOf("/")));
            int year = Integer.parseInt(date.substring(date.lastIndexOf("/")+1,date.length()));
            month--;

            CalendarDay dayy = CalendarDay.from(year, month, day);
            showDatePickerDialog(this, dayy, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int mes = monthOfYear+1;
                    String date = dayOfMonth + "/" +mes +"/" +year;
                    checkOut.setText(date);
                }
            });
        }else{
            CalendarDay day = CalendarDay.today();
            showDatePickerDialog(this, day, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int mes = monthOfYear+1;
                    String date = dayOfMonth + "/" +mes +"/" +year;
                    checkIn.setText(date);
                }
            });
        }
    }

    public static void showDatePickerDialog(Context context, CalendarDay day,
                                            DatePickerDialog.OnDateSetListener callback) {
        if(day == null) {
            day = CalendarDay.today();
        }
        DatePickerDialog dialog = new DatePickerDialog(
                context, 0, callback, day.getYear(), day.getMonth(), day.getDay()
        );

        dialog.show();
    }

    public void onClickSearch(View view) {
        String cityCode = null;
        if(!localName.getText().toString().isEmpty() && !checkIn.getText().toString().isEmpty() && !checkOut.getText().toString().isEmpty()){
            for(int i=0;i< names.size();i++){
                if(names.get(i).equals(localName.getText().toString())){
                    cityCode = codes.get(i);
                }
            }
            String date = checkIn.getText().toString();
            int day = Integer.parseInt(date.substring(0, date.indexOf("/")));
            int month = Integer.parseInt(date.substring(date.indexOf("/") + 1, date.lastIndexOf("/")));
            String startDate = month+"-"+day;
            Date aux = stringToDate(startDate);
            startDate = dateToString(aux);

            date = checkOut.getText().toString();
            day = Integer.parseInt(date.substring(0, date.indexOf("/")));
            month = Integer.parseInt(date.substring(date.indexOf("/") + 1, date.lastIndexOf("/")));
            String endDate = month+"-"+day;
            aux = stringToDate(endDate);
            endDate = dateToString(aux);

            Intent intent = new Intent(this, HotelsListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("cityName", localName.getText().toString());
            bundle.putString("hotelCode", cityCode);
            bundle.putString("guestCount", String.valueOf(guestCount));
            bundle.putString("startDate", startDate);
            bundle.putString("endDate", endDate);
            bundle.putString("maxPrice", String.valueOf(progressValuePrice));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    public static Date stringToDate(String stringDate){
        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("MM-dd");

        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //Se quiser data com hora deve ser passado TRUE como parametro
    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("MM-dd");
        return simpleDateFormat.format(date);
    }

}
