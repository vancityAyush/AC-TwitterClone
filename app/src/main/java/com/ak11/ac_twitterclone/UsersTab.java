package com.ak11.ac_twitterclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class UsersTab extends Fragment implements AdapterView.OnItemClickListener {
    private String username;
    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);
        username = ParseUser.getCurrentUser().getUsername();

        listView = view.findViewById(R.id.usersListView);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_checked,tUsers);
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

        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()){
            FancyToast.makeText(getContext(),tUsers.get(position)+" is now followed!",
                    FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
            ParseUser.getCurrentUser().add("following",tUsers.get(position));
        }
        else {

            FancyToast.makeText(getContext(),tUsers.get(position)+" is now unfollowed!",
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