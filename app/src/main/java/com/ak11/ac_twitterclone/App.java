package com.ak11.ac_twitterclone;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("cnlIlrxr0HZvbl7hYR6PPuIbtirO6Fun7aoYo7Yw")
                // if defined
                .clientKey("rEG76nb0PriL0pxcAM6ZaZs9F6QzhXOAkkx0PC8u")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
