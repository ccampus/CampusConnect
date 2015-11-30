package com.example.sid.campusconnect;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.ApplicationTestCase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseQuery;
import com.parse.ParseSession;

import java.net.PasswordAuthentication;
import java.util.List;

import static com.parse.ParseUser.logInInBackground;



public class MainActivity extends AppCompatActivity {
    public  EditText usernameView;
    public EditText passwordView;
    final Context context = this;
    public static String password;
    public String username1;
    private boolean doubleBackToExitPressedOnce = false;

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
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        usernameView = (EditText) findViewById(R.id.LogName);
        passwordView = (EditText) findViewById(R.id.LogPassword);

        // setting up sign in intent

        findViewById(R.id.SignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(MainActivity.this,Signup.class));

                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.popup_signup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageButton istud = (ImageButton) dialog.findViewById(R.id.PStud);
                ImageButton istaff = (ImageButton) dialog.findViewById(R.id.PStaff);
                //dialog.setTitle("Sign UP!");
                dialog.show();

                istud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,Signup.class));
                    }
                });

                istaff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SignUpAdmin.class));
                    }
                });

            }

        });

        findViewById(R.id.ForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });


        // Set up the submit button click handler
        findViewById(R.id.Login).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                // Validate the log in data
                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder(getResources().getString(R.string.error_intro));

                //empty username field

                if (isEmpty(usernameView)) {
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
                }


                // empty password field


                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
                }
                validationErrorMessage.append(getResources().getString(R.string.error_end));

                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(MainActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                //  progress dialog
                final ProgressDialog dlg = new ProgressDialog(MainActivity.this);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Logging in.  Please wait.");
                dlg.show();

                 // Calling the Parse login method
                    ParseUser.logInInBackground(usernameView.getText().toString(), passwordView.getText().toString(), new LogInCallback() {
                        boolean emailVerified,studVerified,complete;

                        @Override
                        public void done(ParseUser user, ParseException e) {

                            username1=usernameView.getText().toString();
                            password=passwordView.getText().toString();
                            //trial = passwordView.
                            dlg.dismiss();
                            if (e != null) {
                                // Show the error message
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                // Start an intent for the dispatch activity
                                emailVerified = user.getBoolean("emailVerified");
                                studVerified  =user.getBoolean("Is_Verified");
                                complete = user.getBoolean("isComplete");

                                if((emailVerified == true)&&(studVerified==false)&&(complete==false))
                                {
                                    startActivity(new Intent(MainActivity.this,SignUpDetails.class));
                                }

                                if (emailVerified == true) {
                                    //Toast.makeText(MainActivity.this, "Email Yes!", Toast.LENGTH_LONG).show();
                                        if(studVerified==true) {
                                            Toast.makeText(MainActivity.this, "Student yes!", Toast.LENGTH_LONG).show();
                                            if (complete == true) {
                                               // Toast.makeText(MainActivity.this, "Complete!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(MainActivity.this, Home.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                            else {
                                                startActivity(new Intent(MainActivity.this,SignUpDetails.class));
                                            }

                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "Student Verification pending!", Toast.LENGTH_LONG).show();
                                            //ParseUser.getCurrentUser().logOutInBackground();
                                        }

                                } else {
                                    startActivity(new Intent(MainActivity.this, EmailReverify.class));
                                }
                            }

                        }


                    });
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}


