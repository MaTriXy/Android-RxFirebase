[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[ ![Download](https://api.bintray.com/packages/spirosoik/maven/rxfirebase/images/download.svg) ](https://bintray.com/spirosoik/maven/rxfirebase/_latestVersion)

# RxFirebase

RxJava implementation for the Android [Firebase client](https://www.firebase.com/docs/android/).

----

Because the project is not still published in jCenter (waiting for approval) add these to your project dependence:

In parent build.gradle add this:
```
allprojects {
  repositories {
    jcenter()
    maven {
      url  "http://dl.bintray.com/spirosoik/maven"
    }
  }
}
```

In your app build.gradle (or explicit module) you must add this:
```
dependencies {
  compile 'com.soikonomakis:rxfirebase:0.0.1'
}
```

Usage (you can check the sample code also):

```java
public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Firebase firebaseRef =
        new Firebase("https://docs-examples.firebaseio.com/web/saving-data/fireblog/posts");

    RxFirebase.getInstance()
        .observeValueEvent(firebaseRef)
        .subscribeOn(Schedulers.newThread())  // or Android main thread
        .subscribe(new Action1<DataSnapshot>() {
          @Override public void call(DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
              BlogPost newPost = postSnapshot.getValue(BlogPost.class);
            }
          }
        });
  }
}
```

This project still in very early stage, and welcome the pull request
