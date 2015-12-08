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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ViewDiscussionRoom extends ListActivity {

    ArrayList<DiscussionGetterSetter> dislist = new ArrayList<DiscussionGetterSetter>();
    String st;
    String sub;
    String disid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_discussion_room);

        // loading
        final ProgressDialog QSloader = new ProgressDialog(ViewDiscussionRoom.this);
        QSloader.setTitle("Please wait.");
        QSloader.setMessage("Loading Discussion Rooms..");
        QSloader.show();

        ParseUser current_user = ParseUser.getCurrentUser();

        if (current_user != null)
        {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Dis_Member");
            query.whereEqualTo("User_Id", current_user);
            query.include("Dis_Id");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> roomsList, ParseException e) {
                    if (e == null) {
                        int length = roomsList.size();
                        if (length == 0) {
                            Toast toast = Toast.makeText(getApplicationContext(), "No Discussion Rooms!", Toast.LENGTH_LONG);
                            toast.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ViewDiscussionRoom.this, Home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }, 2000);
                        } else {
                            for (ParseObject disrooms : roomsList) {
                                ParseObject dis = disrooms.getParseObject("Dis_Id");
                                try {
                                    sub = dis.fetchIfNeeded().getString("Subject");
                                    Boolean stat = dis.fetchIfNeeded().getBoolean("Status");
                                    if (stat) {
                                        st = "Open";
                                    } else {
                                        st = "Closed";
                                    }
                                    disid = dis.fetchIfNeeded().getObjectId();
                                    dislist.add(new DiscussionGetterSetter(sub, st, disid));
                                    QSloader.dismiss();
                                    ViewDiscussionRoomListAdapter adapter = new ViewDiscussionRoomListAdapter(ViewDiscussionRoom.this, dislist);
                                    setListAdapter(adapter);
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    } else {
                        Log.d("Discussion ", "Error: " + e.getMessage());
                    }
                }
            });

        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gotohome = new Intent(ViewDiscussionRoom.this, Home.class);
                    gotohome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(gotohome);
                }
            }, 2000);
        }

    }

    public void DiscussionDetailHandler(View v)
    {

        View parentView = (View) v.getParent();
        View greatparent = (View)parentView.getParent();
        TextView sd = (TextView)greatparent.findViewById(R.id.disid);
        final String discussion_id = sd.getText().toString();

        Intent intent = new Intent(ViewDiscussionRoom.this,DiscussionDetail.class);
        //NOTE: THE MOST IMP STEP ==> PASSING QuestionID THROUGH INTENT TO NEXT ACTIVITY
        intent.putExtra("discussion_id",discussion_id);
        startActivity(intent);
    }

    public void DiscussionChatHandler(View v)
    {
        View parentView = (View) v.getParent();
        View greatparent = (View)parentView.getParent();
        TextView sd = (TextView)greatparent.findViewById(R.id.disid);
        final String discussion_id = sd.getText().toString();

        Intent intent = new Intent(ViewDiscussionRoom.this,DiscussionDetail.class);
        //NOTE: THE MOST IMP STEP ==> PASSING QuestionID THROUGH INTENT TO NEXT ACTIVITY
        intent.putExtra("discussion_id",discussion_id);
        startActivity(intent);

    }

}
