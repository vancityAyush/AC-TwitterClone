package com.ak11.ac_twitterclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
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



        }
        catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }
}