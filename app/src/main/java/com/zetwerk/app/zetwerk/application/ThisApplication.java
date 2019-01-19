package com.zetwerk.app.zetwerk.application;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by varun.am on 19/01/19
 */
public class ThisApplication extends Application {
    
    private static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    
    public static Context getContext(){
        return context;
    }
}
