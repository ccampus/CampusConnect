package com.example.sid.campusconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
//import java.text.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseAnonymousUtils;
import com.parse.RequestPasswordResetCallback;

import org.w3c.dom.Text;


public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Clear Button Code
        findViewById(R.id.Reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.user_name)).setText("");
            }
        });

        //Submit Button Code
        findViewById(R.id.Submit_forgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username;
                EditText abc = (EditText) findViewById(R.id.user_name);
                username =abc.getText().toString();

                ParseUser.requestPasswordResetInBackground(username, new RequestPasswordResetCallback() {


                    @Override

                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(ForgotPassword.this, "Done", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgotPassword.this, "Undone", Toast.LENGTH_LONG).show();
                        }

                    }

                });
            }
        });

    }

            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
                return true;


            }



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

