package com.soikonomakis.rxfirebasesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.soikonomakis.rxfirebase.RxFirebase;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Firebase firebaseRef =
        new Firebase("https://docs-examples.firebaseio.com/web/saving-data/fireblog/posts");

    RxFirebase.getInstance()
        .observeValueEvent(firebaseRef)
        .subscribeOn(Schedulers.newThread())
        .subscribe(new Action1<DataSnapshot>() {
          @Override public void call(DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
              BlogPost newPost = postSnapshot.getValue(BlogPost.class);
              Toast.makeText(MainActivity.this, newPost.toString(), Toast.LENGTH_LONG).show();
            }
          }
        });
  }
}
