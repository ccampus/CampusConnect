package com.example.sid.campusconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected TextView userprofile;
    protected Button upvotehandler;
    protected Button downvotehandler;
    protected Button writeans;
    protected Button discuss;
    protected Button askstaff;
    protected Button reportqs;
    protected Button editqs;
    protected TextView viewup;
    String quest;
    String s;
    java.sql.Date dob = null;
    ParseObject questionid;
    int upvote_count = 0;
    int downvote_count = 0;

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

        //text links
        userprofile=(TextView)findViewById(R.id.textView27);
        viewup=(TextView)findViewById(R.id.textView28);

        //buttons
        upvotehandler=(Button)findViewById(R.id.Upvote);
        downvotehandler=(Button)findViewById(R.id.Downvote);
        writeans=(Button)findViewById(R.id.WriteAns);
        discuss=(Button)findViewById(R.id.discuss);
        askstaff=(Button)findViewById(R.id.askstaff);
        reportqs=(Button)findViewById(R.id.reportqs);
        editqs=(Button)findViewById(R.id.editqs);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        //Receiving question_id through intent
        Intent intent = getIntent();
        final String question_id = intent.getStringExtra("question_id");
        quest=question_id;

        //  progress dialog
        final ProgressDialog qsloader = new ProgressDialog(QuestionDetail.this);
        qsloader.setTitle("Please wait.");
        qsloader.setMessage("Loading Question Details..");
        qsloader.show();

        //getting question details
        final ParseUser current_user = ParseUser.getCurrentUser();

        if (current_user != null)
        {
            final boolean usertyp;

            if (current_user.getBoolean("Is_Admin") == true) {
                usertyp = true;
            }
            else
            {
                usertyp = false;
                editqs.setVisibility(View.GONE);
            }

            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");

            query.include("User_id");

            query.getInBackground(question_id, new GetCallback<ParseObject>() {
                public void done(ParseObject question, ParseException e) {
                    if (e == null) {
                        questionid = question;

                        //getting userid
                        ParseObject users = question.getParseUser("User_id");
                        String name = users.getString("Name");
                        username.setText(name);

                        s = users.getObjectId();

                        ParseUser c = ParseUser.getCurrentUser();

                        ParseQuery<ParseObject> precheck = ParseQuery.getQuery("Qs_Upvote");
                        precheck.whereEqualTo("Qs_Id", questionid);
                        precheck.whereEqualTo("User_Id", c);
                        precheck.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> resultList, ParseException e) {
                                if (e == null) {
                                    if (resultList.size() == 0) {
                                    } else {
                                        upvotehandler.setText("Upvoted");
                                        upvotehandler.setClickable(false);
                                    }
                                } else {
                                    Log.d("Question", "Error: " + e.getMessage());
                                }
                            }
                        });

                        ParseQuery<ParseObject> predowncheck = ParseQuery.getQuery("Qs_Downvote");
                        predowncheck.whereEqualTo("Qs_Id", questionid);
                        predowncheck.whereEqualTo("User_Id", c);
                        predowncheck.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> resultList, ParseException e) {
                                if (e == null) {
                                    if (resultList.size() == 0) {
                                    } else
                                    {
                                        downvotehandler.setText("Downvoted");
                                        downvotehandler.setClickable(false);
                                    }
                                } else {
                                    Log.d("Question", "Error: " + e.getMessage());
                                }
                            }
                        });

                        String q =question_id;

                        ParseQuery<ParseObject> sup = ParseQuery.getQuery("Question");
                        sup.whereEqualTo("User_id", c);
                        sup.whereEqualTo("objectId",q);
                        sup.findInBackground(new FindCallback<ParseObject>()
                        {
                            public void done(List<ParseObject> reList, ParseException e)
                            {
                                if (e == null)
                                {
                                    if(reList.size()!=0)
                                    {
                                        // implement already reported qs
                                        upvotehandler.setText("Owner");
                                        upvotehandler.setClickable(false);
                                        downvotehandler.setText("Owner");
                                        downvotehandler.setClickable(false);
                                        reportqs.setText("Owner");
                                        reportqs.setClickable(false);
                                        editqs.setVisibility(View.VISIBLE);
                                        editqs.setClickable(true);
                                    }

                                    else
                                    {
                                    }
                                }
                                else
                                {
                                    Log.d("score", "Error: " + e.getMessage());
                                }
                            }
                        });
                                        //getting title
                                        String qstitle = question.getString("Title");
                                        title.setText(qstitle);

                                        //description
                                        String qsdes = question.getString("Description");
                                        des.setText(qsdes);

                                        //fetching isreported
                                        Boolean isreported =question.getBoolean("Is_Reported");
                                        if(isreported)
                                        {
                                            reportqs.setText("Reported");
                                            reportqs.setClickable(false);
                                        }
                                        else
                                        {
                                            // do nothing
                                        }

                                        //if question posted by admin
                                        Boolean byadmin = question.getBoolean("By_Admin");
                                        if(byadmin)
                                        {
                                            reportqs.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            //do nothing
                                        }

                                        //category
                                        String qscat = question.getString("Category");
                                        cat.setText(qscat);

                                        //upvote

                                        upvote_count=question.getInt("Upvote_Count");
                                        String upvoted = String.valueOf(question.getInt("Upvote_Count"));
                                        upvote.setText(upvoted);

                                        //downvote

                                        downvote_count=question.getInt("Downvote_Count");
                                        String down = String.valueOf(question.getInt("Downvote_Count"));
                                        downvote.setText(down);

                                        //usertype
                                        final boolean usert = question.getBoolean("By_Admin");
                                        if (usert) {
                                            usertype.setText("Administrator");
                                        } else {
                                            usertype.setText("Student");
                                        }

                                        //qs added time
                                        Date lastactive = question.getUpdatedAt();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                                        String l = lastactive.toString();
                                        adddate.setText(formatter.format(lastactive).toString());
                                        qsloader.dismiss();
                                    }
                                    else
                                    {
                                        Log.d("Question : Error is : ", e.getMessage());
                                    }
                                }
                            }

                            );
                        }
                        else
                        {
                            Intent gotohome = new Intent(QuestionDetail.this, Home.class);
                            gotohome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(gotohome);
                        }


                        // upvote qs
                        upvotehandler.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //  progress dialog
                                final ProgressDialog dlg = new ProgressDialog(QuestionDetail.this);
                                dlg.setTitle("Please wait.");
                                dlg.setMessage("Upvoting Question.Please wait.");
                                dlg.show();

                                //getting current user
                                // getting qs id
                                // below code :
                                // prevents :
                                // self upvoting
                                // multiple upvotes
                                // it does : increment upvote count
                                // populating qs upvote table
                                // N saving users --> user,qs id values
                                // increment the value in the textview of upvote count

                                final ParseUser current_user = ParseUser.getCurrentUser();
                                final String qs_id = question_id;

                                if (current_user != null) {
                                    ParseQuery<ParseObject> selftupvotingquery = ParseQuery.getQuery("Question");
                                    selftupvotingquery.whereEqualTo("User_id", current_user);
                                    selftupvotingquery.whereEqualTo("objectId", qs_id);
                                    selftupvotingquery.findInBackground(new FindCallback<ParseObject>() {

                                        public void done(List<ParseObject> selfList, ParseException e) {
                                            if (e == null) {
                                                if (selfList.size() != 0) {
                                                    upvotehandler.setText("Owner");
                                                    upvotehandler.setClickable(false);
                                                    dlg.dismiss();
                                                    Toast toast = Toast.makeText(getApplicationContext(), "No Self Upvoting !", Toast.LENGTH_LONG);
                                                    toast.show();
                                                } else
                                                {
                                                    ParseQuery<ParseObject> firstquery = ParseQuery.getQuery("Qs_Upvote");
                                                    firstquery.whereEqualTo("Qs_Id", questionid);
                                                    firstquery.whereEqualTo("User_Id", current_user);
                                                    firstquery.findInBackground(new FindCallback<ParseObject>() {
                                                        public void done(List<ParseObject> questionList, ParseException e) {
                                                            if (e == null) {
                                                                if (questionList.size() == 0) {
                                                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
                                                                    query.getInBackground(qs_id, new GetCallback<ParseObject>() {

                                                                        public void done(ParseObject question, ParseException e) {
                                                                            if (e == null) {
                                                                                question.increment("Upvote_Count");
                                                                                question.saveInBackground(new SaveCallback() {
                                                                                    @Override
                                                                                    public void done(ParseException e) {
                                                                                        if (e == null) {
                                                                                            ParseObject qs_upvote = new ParseObject("Qs_Upvote");
                                                                                            qs_upvote.put("Qs_Id", questionid);
                                                                                            qs_upvote.put("User_Id", current_user);
                                                                                            qs_upvote.saveInBackground(new SaveCallback() {
                                                                                                @Override
                                                                                                public void done(ParseException e) {
                                                                                                    if (e == null)
                                                                                                    {
                                                                                                        HashMap<String, String> params = new HashMap<String, String>();
                                                                                                        params.put("question_id",question_id);
                                                                                                        /*
                                                                                                                Parse.Cloud.define("PointsIncrement", function (request, response) {

                                                                                                            	Parse.Cloud.useMasterKey();
	                                                                                                            var qsid = request.params.question_id;
                                                                                                                var qs = Parse.Object.extend("Question");
                                                                                                                var query = new Parse.Query(qs);

                                                                                                                query.include("User_id");

                                                                                                                query.get(qsid, {

                                                                                                                    success: function (query) {

                                                                                                                        var user = query.get("User_id");
                                                                                                                        var user_id = user.id; // for getting user_id
                                                                                                                        var points = user.get("Points");
                                                                                                                        var rating = user.get("Rating");
                                                                                                                        var newpoints = points + 5;
                                                                                                                        if(newpoints <=100)
                                                                                                                        {
                                                                                                                            rating="n";
                                                                                                                        }
                                                                                                                        else if((newpoints > 100) && (newpoints <=200))
                                                                                                                        {
                                                                                                                        rating="ap";
                                                                                                                        }
                                                                                                                        else if((newpoints > 200)&&(newpoints <=300))
                                                                                                                        {
                                                                                                                            rating="ad";
                                                                                                                        }
                                                                                                                        else if((newpoints>300)&&(newpoints<=400))
                                                                                                                        {
                                                                                                                            rating="e"
                                                                                                                        }
                                                                                                                        else if((newpoints>400)&&(newpoints<=500))
                                                                                                                        {
                                                                                                                            rating="m";
                                                                                                                        }
                                                                                                                        else
                                                                                                                        {
                                                                                                                            rating="m";
                                                                                                                        }
                                                                                                                        var query = new Parse.Query(Parse.User);

                                                                                                                    query.get(user_id).then(function (user) {
                                                                                                                    user.set("Points",newpoints);
                                                                                                                    user.set("Rating",rating);
                                                                                                                    user.save(null, {
                                                                                                                        success: function (user) {
                                                                                                                            response.success(display);
                                                                                                                        },
                                                                                                                        error: function (user, error) {
                                                                                                                            response.error(error);
                                                                                                                        }
                                                                                                                    });
                                                                                                                }).then(function () {
                                                                                                                        response.success(display);
                                                                                                                    },
                                                                                                                    function (error) {
                                                                                                                        response.error(error);
                                                                                                                    });

                                                                                                                    },
                                                                                                                    error: function (query, error) {
                                                                                                                        response.error(error);
                                                                                                                    }
                                                                                                                });
                                                                                                            });
                                                                                                        */

                                                                                                        ParseCloud.callFunctionInBackground("PointsIncrement", params, new FunctionCallback<Map<String, String>>() {
                                                                                                            public void done(Map<String, String> result, ParseException e) {
                                                                                                                if (e == null) {
                                                                                                                    //success
                                                                                                                }
                                                                                                                else
                                                                                                                {
                                                                                                                    Log.d("Error is : ",e.getMessage());
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                        upvote_count = upvote_count + 1;
                                                                                                        String u = String.valueOf(upvote_count);
                                                                                                        upvote.setText(u);
                                                                                                        upvotehandler.setText("Upvoted");
                                                                                                        upvotehandler.setClickable(false);
                                                                                                        dlg.dismiss();
                                                                                                        Toast toast = Toast.makeText(getApplicationContext(), "Question Upvoted Successfully!", Toast.LENGTH_LONG);
                                                                                                        toast.show();
                                                                                                    }
                                                                                                    else {
                                                                                                        Log.d("Upvote Error", e.getMessage());

                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        } else {
                                                                                            Log.d("Upvote Error", e.getMessage());
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });

                                                                } else {
                                                                    upvotehandler.setText("Upvoted");
                                                                    upvotehandler.setClickable(false);
                                                                    dlg.dismiss();
                                                                    Toast toast = Toast.makeText(getApplicationContext(), "Question Already Upvoted!", Toast.LENGTH_LONG);
                                                                    toast.show();

                                                                }
                                                            } else {
                                                                Log.d("score", "Error: " + e.getMessage());
                                                            }
                                                        }
                                                    });
                                                }
                                            } else {
                                                upvotehandler.setText("Owner");
                                                upvotehandler.setClickable(false);
                                                dlg.dismiss();
                                                Toast toast = Toast.makeText(getApplicationContext(), "No Self Upvoting !", Toast.LENGTH_LONG);
                                                toast.show();
                                            }
                                        }
                                    });

                                } else {
                                    Intent gotohome = new Intent(QuestionDetail.this, Home.class);
                                    gotohome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(gotohome);
                                }

                            }
                        });

                        //downvote qs
                        //SAME TO SAME COPY FROM UPVOTE
                        // JUST CHANGE THE VALUES --> RESPECTIVE TABLE COLUMN VALUES
                        // REST REMAINS SAME

                        downvotehandler.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                //  progress dialog
                                final ProgressDialog dlg = new ProgressDialog(QuestionDetail.this);
                                dlg.setTitle("Please wait.");
                                dlg.setMessage("Downvoting Question.Please wait...");
                                dlg.show();


                                final ParseUser current_user = ParseUser.getCurrentUser();
                                final String qs_id = question_id;

                                if (current_user != null) {
                                    ParseQuery<ParseObject> selftupvotingquery = ParseQuery.getQuery("Question");
                                    selftupvotingquery.whereEqualTo("User_id", current_user);
                                    selftupvotingquery.whereEqualTo("objectId", qs_id);
                                    selftupvotingquery.findInBackground(new FindCallback<ParseObject>() {

                                        public void done(List<ParseObject> selfList, ParseException e) {
                                            if (e == null) {
                                                if (selfList.size() != 0) {
                                                    downvotehandler.setText("Owner");
                                                    downvotehandler.setClickable(false);
                                                    dlg.dismiss();
                                                    Toast toast = Toast.makeText(getApplicationContext(), "No Self Downvoting !", Toast.LENGTH_LONG);
                                                    toast.show();

                                                } else {
                                                    ParseQuery<ParseObject> firstquery = ParseQuery.getQuery("Qs_Downvote");
                                                    firstquery.whereEqualTo("Qs_Id", questionid);
                                                    firstquery.whereEqualTo("User_Id", current_user);
                                                    firstquery.findInBackground(new FindCallback<ParseObject>() {
                                                        public void done(List<ParseObject> questionList, ParseException e) {
                                                            if (e == null) {
                                                                if (questionList.size() == 0) {
                                                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
                                                                    query.getInBackground(qs_id, new GetCallback<ParseObject>() {

                                                                        public void done(ParseObject question, ParseException e) {
                                                                            if (e == null) {
                                                                                question.increment("Downvote_Count");
                                                                                question.saveInBackground(new SaveCallback() {
                                                                                    @Override
                                                                                    public void done(ParseException e) {
                                                                                        if (e == null) {
                                                                                            ParseObject qs_upvote = new ParseObject("Qs_Downvote");
                                                                                            qs_upvote.put("Qs_Id", questionid);
                                                                                            qs_upvote.put("User_Id", current_user);
                                                                                            qs_upvote.saveInBackground(new SaveCallback() {
                                                                                                @Override
                                                                                                public void done(ParseException e) {
                                                                                                    if (e == null) {

                                                                                                        HashMap<String, String> params = new HashMap<String, String>();
                                                                                                        params.put("question_id",question_id);
                                                                                                        /*
                                                                                                                Parse.Cloud.define("PointsDerement", function (request, response) {

                                                                                                            	Parse.Cloud.useMasterKey();
	                                                                                                            var qsid = request.params.question_id;
                                                                                                                var qs = Parse.Object.extend("Question");
                                                                                                                var query = new Parse.Query(qs);

                                                                                                                query.include("User_id");

                                                                                                                query.get(qsid, {

                                                                                                                    success: function (query) {

                                                                                                                        var user = query.get("User_id");
                                                                                                                        var user_id = user.id; // for getting user_id
                                                                                                                        var points = user.get("Points");
                                                                                                                        var rating = user.get("Rating");
                                                                                                                        var newpoints = points - 5;
                                                                                                                        if(newpoints <=100)
                                                                                                                        {
                                                                                                                            rating="n";
                                                                                                                        }
                                                                                                                        else if((newpoints > 100) && (newpoints <=200))
                                                                                                                        {
                                                                                                                        rating="ap";
                                                                                                                        }
                                                                                                                        else if((newpoints > 200)&&(newpoints <=300))
                                                                                                                        {
                                                                                                                            rating="ad";
                                                                                                                        }
                                                                                                                        else if((newpoints>300)&&(newpoints<=400))
                                                                                                                        {
                                                                                                                            rating="e"
                                                                                                                        }
                                                                                                                        else if((newpoints>400)&&(newpoints<=500))
                                                                                                                        {
                                                                                                                            rating="m";
                                                                                                                        }
                                                                                                                        else
                                                                                                                        {
                                                                                                                            rating="m";
                                                                                                                        }
                                                                                                                        var query = new Parse.Query(Parse.User);

                                                                                                                    query.get(user_id).then(function (user) {
                                                                                                                    user.set("Points",newpoints);
                                                                                                                    user.set("Rating",rating);
                                                                                                                    user.save(null, {
                                                                                                                        success: function (user) {
                                                                                                                            response.success(display);
                                                                                                                        },
                                                                                                                        error: function (user, error) {
                                                                                                                            response.error(error);
                                                                                                                        }
                                                                                                                    });
                                                                                                                }).then(function () {
                                                                                                                        response.success(display);
                                                                                                                    },
                                                                                                                    function (error) {
                                                                                                                        response.error(error);
                                                                                                                    });

                                                                                                                    },
                                                                                                                    error: function (query, error) {
                                                                                                                        response.error(error);
                                                                                                                    }
                                                                                                                });
                                                                                                            });
                                                                                                        */

                                                                                                        ParseCloud.callFunctionInBackground("PointsDecrement", params, new FunctionCallback<Map<String, String>>() {
                                                                                                            public void done(Map<String, String> result, ParseException e) {
                                                                                                                if (e == null) {
                                                                                                                    //success
                                                                                                                }
                                                                                                                else
                                                                                                                {
                                                                                                                    Log.d("Error is : ",e.getMessage());
                                                                                                                }
                                                                                                            }
                                                                                                        });


                                                                                                        downvotehandler.setText("Downvoted");
                                                                                                        downvotehandler.setClickable(false);
                                                                                                        downvote_count = downvote_count + 1;
                                                                                                        String u = String.valueOf(downvote_count);
                                                                                                        downvote.setText(u);
                                                                                                        dlg.dismiss();
                                                                                                        Toast toast = Toast.makeText(getApplicationContext(), "Question Downvoted Successfully!", Toast.LENGTH_LONG);
                                                                                                        toast.show();

                                                                                                    } else {
                                                                                                        Log.d("Downvote Error", e.getMessage());

                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        } else {
                                                                                            Log.d("Downvote Error", e.getMessage());
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });

                                                                } else {
                                                                    downvotehandler.setText("Downvoted");
                                                                    downvotehandler.setClickable(false);
                                                                    dlg.dismiss();
                                                                    Toast toast = Toast.makeText(getApplicationContext(), "Question Already Downvoted!", Toast.LENGTH_LONG);
                                                                    toast.show();

                                                                }
                                                            } else {
                                                                Log.d("error", "Error: " + e.getMessage());
                                                            }
                                                        }
                                                    });
                                                }
                                            } else {
                                                downvotehandler.setText("Owner");
                                                downvotehandler.setClickable(false);
                                                dlg.dismiss();
                                                Toast toast = Toast.makeText(getApplicationContext(), "No Self Downvoting !", Toast.LENGTH_LONG);
                                                toast.show();

                                            }
                                        }
                                    });

                                } else {
                                    Intent gotohome = new Intent(QuestionDetail.this, Home.class);
                                    gotohome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(gotohome);
                                }
                            }
                        });

                        //report qs

                        reportqs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                // loading
                                final ProgressDialog QSloader = new ProgressDialog(QuestionDetail.this);
                                QSloader.setTitle("Please wait.");
                                QSloader.setMessage("Reporting Question..");
                                QSloader.show();

                                String quest_id = question_id;

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
                                query.getInBackground(quest_id, new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {
                                            object.put("Is_Reported", true);
                                            object.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null)
                                                    {
                                                        HashMap<String, String> params = new HashMap<String, String>();
                                                        params.put("question_id",question_id);
                                                        ParseCloud.callFunctionInBackground("PointsDecrement", params, new FunctionCallback<Map<String, String>>() {
                                                            public void done(Map<String, String> result, ParseException e) {
                                                                if (e == null) {
                                                                    //success
                                                                    reportqs.setText("Reported");
                                                                    reportqs.setClickable(false);
                                                                    QSloader.dismiss();
                                                                    Toast toast = Toast.makeText(getApplicationContext(), "Question Reported Successfully !", Toast.LENGTH_LONG);
                                                                    toast.show();
                                                                } else {
                                                                    Log.d("Error is : ", e.getMessage());
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        Log.d("Error is : ", e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        });

                        //edit question
                        editqs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String questi_id = question_id;
                                Intent intent = new Intent(QuestionDetail.this,EditQuestion.class);
                                intent.putExtra("question_id",questi_id);
                                startActivity(intent);
                            }
                        });

                        // discuss question
                        discuss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String questi_id = question_id;
                                Intent intent = new Intent(QuestionDetail.this,CreateDiscussion.class);
                                intent.putExtra("question_id",questi_id);
                                startActivity(intent);

                            }
                        });
                    }

                public void UserProfileHandler(View v) {
                    Intent intent = new Intent(QuestionDetail.this, UserProfile.class);
                    //NOTE: THE MOST IMP STEP ==> PASSING USERID THROUGH INTENT TO NEXT ACTIVITY
                    intent.putExtra("user_id", s);
                    startActivity(intent);
                }

                public void ViewUserUpvotesHandler(View v)
                {
                    Intent intent = new Intent(QuestionDetail.this,ViewUpvotes.class);
                    intent.putExtra("question_id",quest);
                    startActivity(intent);
                }

            }
