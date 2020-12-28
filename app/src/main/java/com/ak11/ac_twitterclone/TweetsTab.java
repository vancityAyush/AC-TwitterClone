package com.ak11.ac_twitterclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TweetsTab extends Fragment {
    private ListView listView;
    final ArrayList<HashMap<String,String>> tweetList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tweets_tab, container, false);

        listView = view.findViewById(R.id.tweetsListView);

        final SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),tweetList,
                android.R.layout.simple_list_item_2, new String[]{"tweetUsername","tweetValue"},
                new int[]{android.R.id.text1, android.R.id.text2});
        try{
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Tweet");
            parseQuery.whereContainedIn("username", ParseUser.getCurrentUser().getList("following"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseObject tweetObject: objects){
                            HashMap<String,String> userTweet = new HashMap<>();
                            userTweet.put("tweetUsername",tweetObject.getString("username"));
                            userTweet.put("tweetValue",tweetObject.getString("tweet"));
                            tweetList.add(userTweet);

                        }
                        listView.setAdapter(simpleAdapter);
                    }
                }
            });



        }
        catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }
}