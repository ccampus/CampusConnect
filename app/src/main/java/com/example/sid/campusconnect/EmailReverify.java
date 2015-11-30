package com.example.sid.campusconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.ParseSession;

import java.net.PasswordAuthentication;
import java.util.List;

import javax.security.auth.callback.Callback;

import bolts.Task;

public class EmailReverify extends MainActivity {
    private String email;
    private String username;
    private String passkey,object_id,usid;

    //MainActivity globalvar = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reverify);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username=ParseUser.getCurrentUser().getUsername();
        email=ParseUser.getCurrentUser().getEmail();
        object_id=ParseUser.getCurrentUser().getObjectId();
        //passkey=globalvar.getPass();//NULL VALUE militye.
        passkey = MainActivity.password;

        findViewById(R.id.btn_Resend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            ParseUser user= ParseUser.getCurrentUser();
            user.setEmail("");
                try {
                    user.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
				final ProgressDialog dlg = new ProgressDialog(EmailReverify.this);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Sending email.  Please wait.");
                dlg.show();

                user.setEmail(email);
                try {
                    user.save();
                    Toast.makeText(EmailReverify.this, "Email for account verification has been sent to "+email, Toast.LENGTH_LONG).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
				logout();

            }


        });

       /* ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.whereEqualTo("playerName", "Dan Stemkoski");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });*/


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
          logout();
            }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email_reverify, menu);
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

    @Override
    public void onStop(){
        super.onStop();
        logout();
    }

    public void logout()
    {
        ParseUser.getCurrentUser().logOut();
       // Intent intent = new Intent(EmailReverify.this,checker.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        //startActivity(intent);
    }

    public void logout1()
    {
        ParseUser.getCurrentUser().logOut();
        Intent intent = new Intent(EmailReverify.this,checker.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }


}