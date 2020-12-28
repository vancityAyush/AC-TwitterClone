package com.ak11.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.logout_item:
               ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                   @Override
                   public void done(ParseException e) {
                       FancyToast.makeText(MainActivity.this,
                               ParseUser.getCurrentUser().getUsername()+" logged out!", Toast.LENGTH_SHORT,
                               FancyToast.INFO,false).show();
                       startActivity(new Intent(MainActivity.this, SignUp.class));
                       finish();
                   }
               });
               break;
           case R.id.send_item:
               final AlertDialog.Builder alert = new AlertDialog.Builder(this);
               View mView = getLayoutInflater().inflate(R.layout.my_dialog,null);

               final EditText edtTweet = mView.findViewById(R.id.edtTweet);
               Button btnSendTweet = mView.findViewById(R.id.btnSendTweet);
               Button btnCancel = mView.findViewById(R.id.btnCancel);

               alert.setView(mView);
               final  AlertDialog alertDialog = alert.create();
               alertDialog.setCanceledOnTouchOutside(false);
               btnSendTweet.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       ParseObject parseObject = new ParseObject("Tweet");
                       parseObject.put("tweet",edtTweet.getText().toString());
                       parseObject.put("user",ParseUser.getCurrentUser());
                       final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                       progressDialog.setMessage("Sending...");
                       progressDialog.show();
                       parseObject.saveInBackground(new SaveCallback() {
                           @Override
                           public void done(ParseException e) {
                               if(e==null) {
                                   FancyToast.makeText(MainActivity.this
                                           , ParseUser.getCurrentUser().getUsername() + "'s tweet " + edtTweet.getText() +
                                                   " is saved!!!", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                   alertDialog.dismiss();
                               }
                               else
                                   FancyToast.makeText(MainActivity.this
                                           ,e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                               progressDialog.dismiss();

                           }
                       });


                   }
               });

               btnCancel.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       alertDialog.dismiss();
                   }
               });
               alertDialog.show();
               break;
       }

        return super.onOptionsItemSelected(item);
    }


}