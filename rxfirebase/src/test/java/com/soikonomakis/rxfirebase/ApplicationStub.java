package com.soikonomakis.rxfirebase;

import android.app.Application;
import com.firebase.client.Config;
import com.firebase.client.Firebase;
import com.firebase.client.Logger;

public class ApplicationStub extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Firebase.setAndroidContext(this);
  }
}
