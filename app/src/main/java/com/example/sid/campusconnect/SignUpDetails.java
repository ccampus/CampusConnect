package com.example.sid.campusconnect;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class SignUpDetails extends AppCompatActivity {
    final Context context = this;
    CalendarView calendar;
    Date dob=null;
    Dialog dialog ;
    ParseUser user;
    StringBuilder name;
    EditText fname,lname;
    String dept,gender;
    Spinner sdept,sgender;
    Bitmap imv1,imv2,imv3;
    private boolean doubleBackToExitPressedOnce = false ;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);
        user=ParseUser.getCurrentUser();

        fname=(EditText) findViewById(R.id.edt_Fname);
        lname=(EditText) findViewById(R.id.edt_Lname);
        sdept=(Spinner) findViewById(R.id.spinner_dept);
        sgender=(Spinner) findViewById(R.id.spinner_gender);





        findViewById(R.id.sign_btn_dob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                calendar = (CalendarView) dialog.findViewById(R.id.calendar);
                dialog.setContentView(R.layout.popup_dob);
                dialog.show();
                initializeCalendar();
            }
        });


        findViewById(R.id.sign_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imv1 = BitmapFactory.decodeResource(getResources(), R.drawable.male);
                imv2 = BitmapFactory.decodeResource(getResources(), R.drawable.female);
                imv3 = BitmapFactory.decodeResource(getResources(), R.drawable.unspecified);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                final ProgressDialog dlg = new ProgressDialog(SignUpDetails.this);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Updating Profile. A moment please...");
                dlg.show();


                String st=fname.getText().toString()+" "+lname.getText().toString();


                dept = String.valueOf(sdept.getSelectedItem());
                gender = String.valueOf(sgender.getSelectedItem());




               if(gender.equals("Male"))
                {
                    imv1.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] data = bos.toByteArray();
                    ParseFile file = new ParseFile("dp.png",data);
                    user.put("Profile_pic",file);
                    file.saveInBackground();
                    //user.saveInBackground();
                }
                else if(gender.equals("Female"))
                {
                    imv2.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] data = bos.toByteArray();
                    ParseFile file = new ParseFile("dp.png",data);
                    user.put("Profile_pic",file);
                    file.saveInBackground();
                   // user.saveInBackground();
                }
                else
                {
                    imv3.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] data = bos.toByteArray();
                    ParseFile file = new ParseFile("dp.png",data);
                    user.put("Profile_pic",file);
                    file.saveInBackground();
                    //user.saveInBackground();
                }

                user.put("Name", st);
                user.put("Dept", dept);
                user.put("Gender", gender);
                user.put("Dob", dob);
                user.put("isComplete", true);
                try {
                    user.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Toast.makeText(SignUpDetails.this,"Details Updated", Toast.LENGTH_LONG).show();
                dlg.hide();

                Boolean verified;
                verified=user.getBoolean("isVerified");

                    Intent intent = new Intent(SignUpDetails.this, Proceed.class);
                    startActivity(intent);
            }
        });

    }


    public void initializeCalendar() {
        calendar = (CalendarView) dialog.findViewById(R.id.calendar);
        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);
        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);
        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);
        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + (month+1) + "/" + year, Toast.LENGTH_LONG).show();
                dob=new Date(year,month,day);
                dialog.hide();
            }
        });
    }

}
