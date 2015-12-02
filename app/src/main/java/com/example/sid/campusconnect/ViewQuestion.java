package com.example.sid.campusconnect;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ViewQuestion extends ListActivity {

    protected List<ParseObject> mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        // loading
        final ProgressDialog QSloader = new ProgressDialog(ViewQuestion.this);
        QSloader.setTitle("Please wait.");
        QSloader.setMessage("Loading Questions..");
        QSloader.show();

        ParseUser current_user = ParseUser.getCurrentUser();

        if (current_user != null)
        {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
            query.findInBackground(new FindCallback<ParseObject>()
            {
                public void done(List<ParseObject> questionList, ParseException e) {
                    if (e == null)
                    {
                        int length = questionList.size();
                        if(length==0)
                        {
                            Toast toast = Toast.makeText(getApplicationContext(),"No Questions!",Toast.LENGTH_LONG);
                            toast.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ViewQuestion.this, Home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }, 5000);
                        }
                        else
                        {
                            QSloader.dismiss();
                            mStatus = questionList;
                            ViewQuestionListAdapter adapter = new ViewQuestionListAdapter(getListView().getContext(), mStatus);
                            setListAdapter(adapter);
                        }
                    }
                    else
                    {
                        Log.d("Question ", "Error: " + e.getMessage());
                    }
                }
            });

        } else
        {
            Intent intent = new Intent(ViewQuestion.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void ReadFullQsHandler(View v)
    {

        View parentView = (View) v.getParent();
        TextView sd = (TextView)parentView.findViewById(R.id.QsId);
        final String question_id = sd.getText().toString();

        Intent intent = new Intent(ViewQuestion.this,QuestionDetail.class);
        //NOTE: THE MOST IMP STEP ==> PASSING QuestionID THROUGH INTENT TO NEXT ACTIVITY
        intent.putExtra("question_id", question_id);
        startActivity(intent);
    }

}

