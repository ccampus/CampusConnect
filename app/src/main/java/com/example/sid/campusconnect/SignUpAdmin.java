package com.example.sid.campusconnect;

import android.app.ProgressDialog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpAdmin extends AppCompatActivity {

    private EditText usernameView;
    private EditText passwordView;
    private EditText passwordAgainView;
    private EditText email;
    private EditText rcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the signup form.
        email = (EditText) findViewById(R.id.regEmail);
        passwordView = (EditText) findViewById(R.id.regPassword);
        passwordAgainView = (EditText) findViewById(R.id.regAgainPassword);
        usernameView=(EditText) findViewById(R.id.regName);
        rcode=(EditText) findViewById(R.id.regCode);

        // Set up the create account button click handler
        findViewById(R.id.signup).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        // Validate the sign up data
                        boolean validationError = false;

                        StringBuilder validationErrorMessage =
                                new StringBuilder(getResources().getString(R.string.error_intro));

                        //empty username


                        if (isEmpty(usernameView)) {
                            validationError = true;
                            validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
                        }

                        // empty password

                        if (isEmpty(passwordView)) {
                            if (validationError) {
                                validationErrorMessage.append(getResources().getString(R.string.error_join));
                            }
                            validationError = true;
                            validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
                        }

                        //empty email address

                        if(isEmpty(email))
                        {
                            validationError=true;
                            validationErrorMessage.append(getResources().getString(R.string.error_blank_email));
                        }

                        if (!isMatching(passwordView, passwordAgainView)) {
                            if (validationError) {
                                validationErrorMessage.append(getResources().getString(R.string.error_join));
                            }
                            validationError = true;
                            validationErrorMessage.append(getResources().getString(
                                    R.string.error_mismatched_passwords));
                        }
                        validationErrorMessage.append(getResources().getString(R.string.error_end));

                        if (!((EditText) findViewById(R.id.regCode)).getText().toString().equals("1111")) {
                            validationError = true;
                            validationErrorMessage.append(getResources().getString(R.string.error_wrong_code));
                        }
                        // If there is a validation error, display the error
                        if (validationError) {
                            Toast.makeText(SignUpAdmin.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                                    .show();
                            ((EditText) findViewById(R.id.regPassword)).setText("");
                            ((EditText) findViewById(R.id.regAgainPassword)).setText("");
                            ((EditText) findViewById(R.id.regCode)).setText("");
                            return;
                        }


                        // Set up a progress dialog
                        final ProgressDialog dlg = new ProgressDialog(SignUpAdmin.this);
                        dlg.setTitle("Please wait.");
                        dlg.setMessage("Signing up.  Please wait.");
                        dlg.show();

                        ParseUser user = new ParseUser();
                        user.setUsername(usernameView.getText().toString().trim());
                        user.setPassword(passwordView.getText().toString().trim());
                        user.setEmail(email.getText().toString().trim());
                        user.put("Is_Admin", true);
                        user.put("Is_Verified",true);
                        user.put("Points",0);
                        user.put("Rating","n");


                        // Calling the Parse signup method
                        user.signUpInBackground(new SignUpCallback() {

                            @Override
                            public void done(ParseException e) {
                                dlg.dismiss();
                                if (e != null) {
                                    //  error message
                                    Toast.makeText(SignUpAdmin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    ((EditText) findViewById(R.id.regPassword)).setText("");
                                    ((EditText) findViewById(R.id.regAgainPassword)).setText("");
                                    ((EditText) findViewById(R.id.regCode)).setText("");
                                } else
                                {
                                    // Start an intent for the dispatch activity
                                    Toast.makeText(SignUpAdmin.this, "Email for account Confirmation has been sent to " + ParseUser.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();

                                    ParseUser.getCurrentUser().logOutInBackground();
                                    ((EditText) findViewById(R.id.regEmail)).setText("");
                                    ((EditText) findViewById(R.id.regPassword)).setText("");
                                    ((EditText) findViewById(R.id.regAgainPassword)).setText("");
                                    ((EditText) findViewById(R.id.regName)).setText("");
                                    ((EditText) findViewById(R.id.regCode)).setText("");
                                }
                            }
                        });
                    }
                });



        findViewById(R.id.Reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.regEmail)).setText("");
                ((EditText) findViewById(R.id.regPassword)).setText("");
                ((EditText) findViewById(R.id.regAgainPassword)).setText("");
                ((EditText) findViewById(R.id.regName)).setText("");
                ((EditText) findViewById(R.id.regCode)).setText("");

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

    private boolean isMatching(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }


    }



}
