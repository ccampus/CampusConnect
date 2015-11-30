package com.example.sid.campusconnect;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Profile extends AppCompatActivity {

        private static int RESULT_LOAD_IMAGE = 1;
    ParseUser user;
    private  TextView email,dept,username,name;
    ImageView imageView;
    String img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email = (TextView) findViewById(R.id.pro_email);
        dept =  (TextView) findViewById(R.id.pro_dept);
        username= (TextView) findViewById(R.id.pro_uname);
        name= (TextView) findViewById(R.id.pro_fullname);


        user=ParseUser.getCurrentUser();
        email.setText(user.getEmail().toString());
        username.setText(user.getUsername().toString());
        dept.setText(user.get("Dept").toString());
        name.setText(user.get("Name").toString());

        imageView = (ImageView) findViewById(R.id.imgview);
        ParseFile imageFile = (ParseFile) user.get("Profile_pic");

        try
        {
            img_url = imageFile.getUrl();
            byte[] bitmapdata = imageFile.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata.length);
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e1)
        {
            img_url="";
            Toast.makeText(Profile.this, "No Dp for this Student", Toast.LENGTH_LONG);
        }
        findViewById(R.id.pro_btn_dp).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View arg0) {

               Intent i = new Intent(
                       Intent.ACTION_PICK,
                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
           }
       });

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(Profile.this,Home.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Bitmap bitmap=null;

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            ImageView imageView = (ImageView) findViewById(R.id.imgview);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();

            ParseFile file=new ParseFile("dp.png",image);
            file.saveInBackground();

            user.put("Profile_pic", file);
            user.saveInBackground();
            Toast.makeText(Profile.this, "File Uploaded", Toast.LENGTH_SHORT).show();
            imageView.setImageURI(selectedImage);

        }
    }

}

