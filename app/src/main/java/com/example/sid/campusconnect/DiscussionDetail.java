package com.example.sid.campusconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DiscussionDetail extends Activity {

    LinearLayout container;
    String sub,st,username,opened;
   ArrayList<String> ms = new ArrayList<String>();
    protected TextView s;
    protected TextView sta;
    protected TextView open;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_detail);

        s=(TextView)findViewById(R.id.discussTopic);
        sta=(TextView)findViewById(R.id.discussStatus);
        open=(TextView)findViewById(R.id.discussOpenedBy);
        container=(LinearLayout)findViewById(R.id.mem);

        //Receiving discussion_id through intent
        Intent intent = getIntent();
        final String discussion_id = intent.getStringExtra("discussion_id");

        // loading
        final ProgressDialog QSloader = new ProgressDialog(DiscussionDetail.this);
        QSloader.setTitle("Please wait.");
        QSloader.setMessage("Loading Discussion Room Details..");
        QSloader.show();

        ParseUser current_user = ParseUser.getCurrentUser();

        if (current_user != null)
        {
            // getting discussion info
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Discuss_Room");
            query.include("Opened_By");
            query.getInBackground(discussion_id, new GetCallback<ParseObject>() {
                public void done(ParseObject dis, ParseException e) {
                    if (e == null) {
                        QSloader.dismiss();
                        sub = dis.getString("Subject");
                        s.setText(sub);

                        Boolean result = dis.getBoolean("Status");
                        if (result) {
                            st = "Open";
                            sta.setText(st);
                        } else {
                            st = "Closed";
                            sta.setText(st);
                        }
                        ParseObject users = dis.getParseObject("Opened_By");
                        try {
                            opened = users.fetchIfNeeded().getString("Name");
                            open.setText(opened);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        // getting members list
                        ParseQuery<ParseObject> querys = ParseQuery.getQuery("Dis_Member");
                        querys.whereEqualTo("Dis_Id", dis);
                        querys.include("User_Id");
                        querys.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> memberList, ParseException e) {
                                if (e == null) {
                                    for (ParseObject members : memberList) {
                                        ParseObject mem = members.getParseObject("User_Id");
                                        try {
                                            username = mem.fetchIfNeeded().getString("Name");
                                            TextView td = new TextView(DiscussionDetail.this);
                                            td.setText(username);
                                            td.setPadding(10, 10, 10, 10);
                                            //td.setTextColor(Color.BLACK);
                                            //td.setTextAppearance(DiscussionDetail.this,android.R.style.TextAppearance_DeviceDefault_Medium);
                                            container.addView(td);
                                            //ms.add(username);
                                            //Toast toast =Toast.makeText(getApplicationContext(),username,Toast.LENGTH_LONG);
                                            //toast.show();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    // for testing
                                    //Toast toast =Toast.makeText(getApplicationContext(),sub+st+opened,Toast.LENGTH_LONG);
                                    //toast.show();
                                } else {
                                    Log.d("Disussion", "Error: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        Log.d("Error is : ", e.getMessage());
                    }
                }
            });

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gotohome = new Intent(DiscussionDetail.this, Home.class);
                    gotohome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(gotohome);
                }
            }, 2000);
        }
    }

}
