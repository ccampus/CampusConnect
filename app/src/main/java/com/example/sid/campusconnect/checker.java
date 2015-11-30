package com.example.sid.campusconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * Created by Sid on 28-Aug-15.
 */
public class checker extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            ParseUser user = ParseUser.getCurrentUser();
            if(user.getBoolean("isComplete")==true) {

                if (user.getBoolean("Is_Verified") == false) {
                    user.logOut();
                    Intent intent = new Intent(checker.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else {
                    startActivity(new Intent(checker.this, Home.class));
                }
            }
            else
            {
                startActivity(new Intent(checker.this, SignUpDetails.class));
            }

        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(checker.this, MainActivity.class));
        }
    }
}
