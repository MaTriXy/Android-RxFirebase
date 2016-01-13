[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[ ![Download](https://api.bintray.com/packages/spirosoik/maven/rxfirebase/images/download.svg) ](https://bintray.com/spirosoik/maven/rxfirebase/_latestVersion)
[![Build Status](https://circleci.com/gh/spirosoik/Android-RxFirebase/tree/master.svg?style=shield&circle-token=55bc5669774dd618582c18e1a6b3a8d4e3ae1de7)](https://circleci.com/gh/spirosoik/Android-RxFirebase/tree/master)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxFirebase-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3000)
# RxFirebase

RxJava implementation for the Android [Firebase client](https://www.firebase.com/docs/android/).

----

The project is available on jCenter. In your app build.gradle (or explicit module) you must add this:
```
dependencies {
  compile 'com.soikonomakis:rxfirebase:0.0.2'
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

This project still in very early stage and you are welcome to send a pull request.

License
-------

    Copyright 2016 Economakis Spyridon

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
