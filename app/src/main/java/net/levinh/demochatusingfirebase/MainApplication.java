package net.levinh.demochatusingfirebase;

import android.app.Application;


import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by levinhtxbt@gmail.com on 13/09/2016.
 */
public class MainApplication extends Application {

    public static final String URL_DATABASE = "https://chat-5291a.firebaseio.com";
    @Override
    public void onCreate() {
        super.onCreate();
        //Firebase.setAndroidContext(this);
    }
}
