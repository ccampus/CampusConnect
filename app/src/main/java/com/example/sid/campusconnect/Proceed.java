package com.example.sid.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.parse.ParseUser;

public class Proceed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ParseUser.getCurrentUser().getBoolean("Is_Verified") == true) {
            startActivity(new Intent(Proceed.this, Home.class));
        }
        else{
            ParseUser.getCurrentUser().logOutInBackground();
            startActivity(new Intent(Proceed.this, SessionChecker.class));
        }
    }

}
