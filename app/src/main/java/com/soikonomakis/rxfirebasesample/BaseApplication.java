package com.soikonomakis.rxfirebasesample;

import android.app.Application;
import com.firebase.client.Firebase;

/**
 * Created by Spiros I. Oikonomakis on 04/01/16.
 */
public class BaseApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();

    Firebase.setAndroidContext(this);
  }
}
