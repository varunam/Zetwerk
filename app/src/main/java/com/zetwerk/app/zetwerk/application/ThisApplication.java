package com.zetwerk.app.zetwerk.application;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by varun.am on 19/01/19
 */
public class ThisApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
