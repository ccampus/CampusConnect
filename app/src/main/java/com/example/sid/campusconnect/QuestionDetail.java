package com.example.sid.campusconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

public class QuestionDetail extends AppCompatActivity
{
    protected TextView title;
    protected TextView des;
    protected TextView cat;
    protected TextView username;
    protected TextView upvote;
    protected TextView downvote;
    protected TextView adddate;
    protected TextView usertype;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        title=(TextView)findViewById(R.id.textView26);
        des=(TextView)findViewById(R.id.textView25);
        cat=(TextView)findViewById(R.id.textView24);
        username=(TextView)findViewById(R.id.textView23);
        upvote=(TextView)findViewById(R.id.textView22);
        downvote=(TextView)findViewById(R.id.textView21);
        adddate=(TextView)findViewById(R.id.textView20);
        usertype=(TextView)findViewById(R.id.textView19);

        //Receiving question_id through intent
        Intent intent = getIntent();
        final String question_id = intent.getStringExtra("question_id");

        //  progress dialog
        final ProgressDialog qsloader = new ProgressDialog(QuestionDetail.this);
        qsloader.setTitle("Please wait.");
        qsloader.setMessage("Loading Question Details..");
        qsloader.show();

        //getting question details
        ParseUser current_user = ParseUser.getCurrentUser();

        if (current_user != null)
        {
            final boolean usertyp;

            if (current_user.getBoolean("Is_Admin") == true) {
                usertyp = true;
            }
            else
            {
                usertyp = false;
            }

            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");

            query.include("User_id");

            query.getInBackground(question_id, new GetCallback<ParseObject>()
            {
                public void done(ParseObject question, ParseException e)
                {
                    if (e == null)
                    {
                        //getting userid
                        ParseObject users = question.getParseUser("User_id");
                        String name = users.getString("Name");
                        username.setText(name);

                        user_id=users.getString("objectId");

                        //getting title
                      String qstitle = question.getString("Title");
                      title.setText(qstitle);

                        //description
                        String qsdes = question.getString("Description");
                        des.setText(qsdes);

                        //category
                        String qscat = question.getString("Category");
                        cat.setText(qscat);

                        //upvote
                        String upvoted = String.valueOf(question.getInt("Upvote_Count"));
                        upvote.setText(upvoted);

                        //downvote
                        String down = String.valueOf(question.getInt("Downvote_Count"));
                        downvote.setText(down);

                        //usertype
                        final boolean usert=question.getBoolean("By_Admin");
                        if(usert)
                        {
                        usertype.setText("Administrator");
                        }
                        else
                        {
                            usertype.setText("Student");
                        }

                        //qs added time
                        Date lastactive=question.getUpdatedAt();
                        String l=lastactive.toString();
                        adddate.setText(l);
                        qsloader.dismiss();
                    }
                    else
                    {
                        Log.d("Question : Error is : ", e.getMessage());
                    }
                }
            });
        }
        else
        {
            Intent gotohome = new Intent(QuestionDetail.this, Home.class);
            gotohome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(gotohome);
        }

    }

    public void UserProfileHandler(View v)
    {

        Intent intent = new Intent(QuestionDetail.this,UserProfile.class);
        //NOTE: THE MOST IMP STEP ==> PASSING USERID THROUGH INTENT TO NEXT ACTIVITY
        intent.putExtra("user_id",user_id);
        startActivity(intent);

    }

}
