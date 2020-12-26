package com.ak11.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String username;
    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        username = ParseUser.getCurrentUser().getUsername();

        listView = findViewById(R.id.listView);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo("emailVerified", true);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseUser twitterUser : objects) {
                            tUsers.add(twitterUser.getUsername());
                        }
                        listView.setAdapter(adapter);
                        for(String twitterUser : tUsers){
                            if(ParseUser.getCurrentUser().getList("following")!=null)
                            if(ParseUser.getCurrentUser().getList("following").contains(twitterUser)) {
                                listView.setItemChecked(tUsers.indexOf(twitterUser), true);
                            }
                        }
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
                       FancyToast.makeText(TwitterUsers.this,username+" logged out!", Toast.LENGTH_SHORT,
                               FancyToast.INFO,false).show();
                       startActivity(new Intent(TwitterUsers.this,SignUp.class));
                       finish();
                   }
               });
               break;
       }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this,tUsers.get(position)+" is now followed!",
                    FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
            ParseUser.getCurrentUser().add("following",tUsers.get(position));
        }
        else {

            FancyToast.makeText(TwitterUsers.this,tUsers.get(position)+" is now unfollowed!",
                    FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
            ParseUser.getCurrentUser().getList("following").remove(tUsers.get(position));
            List currentList = ParseUser.getCurrentUser().getList("following");
            ParseUser.getCurrentUser().put("following",currentList);
        }
        try {
            ParseUser.getCurrentUser().save();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}