/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soikonomakis.rxfirebase;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.soikonomakis.rxfirebase.FirebaseChildEvent.EventType;
import com.soikonomakis.rxfirebase.exceptions.FirebaseAuthProviderDisabledException;
import com.soikonomakis.rxfirebase.exceptions.FirebaseExpiredTokenException;
import com.soikonomakis.rxfirebase.exceptions.FirebaseInvalidTokenException;
import com.soikonomakis.rxfirebase.exceptions.FirebaseNetworkErrorException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * The class is used as wrapper to firebase functionlity with
 * RxJava
 */
public class RxFirebase {

  private static RxFirebase instance;

  /**
   * Singleton
   *
   * @return {@link RxFirebase}
   */
  public static synchronized RxFirebase getInstance() {
    if (instance == null) {
      instance = new RxFirebase();
    }
    return instance;
  }

  /**
   * Attempts to authenticate to Firebase with an OAuth token from a provider supported by Firebase
   * Login. This method only works for providers that only require a 'access_token' as a parameter
   *
   * @param ref {@link com.firebase.client.Query} this is reference of a Firebase Query
   * @param token {@link String} this is the access token for the give provider
   * @param provider {@link String} this is the given provider for login
   * @return an {@link rx.Observable} of {@link com.firebase.client.AuthData} to use
   */
  public Observable<AuthData> observeAuthWithOauthToken(final Firebase ref,
      final String token, final String provider) {
    return Observable.create(new Observable.OnSubscribe<AuthData>() {
      @Override public void call(final Subscriber<? super AuthData> subscriber) {
        ref.authWithOAuthToken(provider, token, new Firebase.AuthResultHandler() {
          @Override public void onAuthenticated(AuthData authData) {
            subscriber.onNext(authData);
            subscriber.onCompleted();
          }

          @Override public void onAuthenticationError(FirebaseError firebaseError) {
            switch (firebaseError.getCode()) {
              case FirebaseError.INVALID_TOKEN:
                subscriber.onError(new FirebaseInvalidTokenException(firebaseError.getMessage()));
                break;
              case FirebaseError.AUTHENTICATION_PROVIDER_DISABLED:
                subscriber.onError(
                    new FirebaseAuthProviderDisabledException(firebaseError.getMessage()));
                break;
              case FirebaseError.EXPIRED_TOKEN:
                subscriber.onError(new FirebaseExpiredTokenException(firebaseError.getMessage()));
                break;
              case FirebaseError.NETWORK_ERROR:
                subscriber.onError(new FirebaseNetworkErrorException(firebaseError.getMessage()));
                break;
              default:
                subscriber.onError(new FirebaseException(firebaseError.getMessage()));
            }
          }
        });
      }
    });
  }

  /**
   * This methods observes a firebase query and returns back
   * an Observable of the {@link DataSnapshot}
   * when the firebase client uses a {@link ValueEventListener}
   *
   * @param ref {@link com.firebase.client.Query} this is reference of a Firebase Query
   * @return an {@link rx.Observable} of datasnapshot to use
   */
  public Observable<DataSnapshot> observeValueEvent(final Query ref) {
    return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
      @Override public void call(final Subscriber<? super DataSnapshot> subscriber) {
        final ValueEventListener listener = new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            subscriber.onNext(dataSnapshot);
          }

          @Override public void onCancelled(FirebaseError error) {
            // Turn the FirebaseError into a throwable to conform to the API
            subscriber.onError(new FirebaseException(error.getMessage()));
          }
        };

        ref.addValueEventListener(listener);

        // When the subscription is cancelled, remove the listener
        subscriber.add(Subscriptions.create(new Action0() {
          @Override public void call() {
            ref.removeEventListener(listener);
          }
        }));
      }
    });
  }

  /**
   * This methods observes a firebase query and returns back ONCE
   * an Observable of the {@link DataSnapshot}
   * when the firebase client uses a {@link ValueEventListener}
   *
   * @param ref {@link Query} this is reference of a Firebase Query
   * @return an {@link rx.Observable} of datasnapshot to use
   */
  public Observable<DataSnapshot> observeSingleValue(final Query ref) {
    return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
      @Override public void call(final Subscriber<? super DataSnapshot> subscriber) {
        final ValueEventListener valueEventListener = new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            subscriber.onNext(dataSnapshot);
          }

          @Override public void onCancelled(FirebaseError error) {
            // Turn the FirebaseError into a throwable to conform to the API
            subscriber.onError(new FirebaseException(error.getMessage()));
          }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);

        // When the subscription is cancelled, remove the listener
        subscriber.add(Subscriptions.create(new Action0() {
          @Override public void call() {
            ref.removeEventListener(valueEventListener);
          }
        }));
      }
    });
  }

  /**
   * This methods observes a firebase query and returns back
   * an Observable of the {@link DataSnapshot}
   * when the firebase client uses a {@link ChildEventListener}
   *
   * @param ref {@link Query} this is reference of a Firebase Query
   * @return an {@link rx.Observable} of {@link FirebaseChildEvent}
   * to use
   */
  public Observable<FirebaseChildEvent> observeChildEvent(final Query ref) {
    return Observable.create(new Observable.OnSubscribe<FirebaseChildEvent>() {
      @Override public void call(final Subscriber<? super FirebaseChildEvent> subscriber) {
        final ChildEventListener childEventListener = new ChildEventListener() {

          @Override public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            subscriber.onNext(
                new FirebaseChildEvent(dataSnapshot, previousChildName, EventType.ADDED));
          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            subscriber.onNext(
                new FirebaseChildEvent(dataSnapshot, previousChildName, EventType.CHANGED));
          }

          @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
            subscriber.onNext(new FirebaseChildEvent(dataSnapshot, EventType.REMOVED));
          }

          @Override public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            subscriber.onNext(
                new FirebaseChildEvent(dataSnapshot, previousChildName, EventType.MOVED));
          }

          @Override public void onCancelled(FirebaseError firebaseError) {
            subscriber.onError(new FirebaseException(firebaseError.getMessage()));
          }
        };
        ref.addChildEventListener(childEventListener);
        // this is used to remove the listener when the subscriber is
        // cancelled (unsubscribe)
        subscriber.add(Subscriptions.create(new Action0() {
          @Override public void call() {
            ref.removeEventListener(childEventListener);
          }
        }));
      }
    });
  }

  /**
   * Creates an observable only for the child added method
   *
   * @param ref {@link Query} this is reference of a Firebase Query
   * @return an {@link rx.Observable} of {@link FirebaseChildEvent}
   * to use
   */
  public Observable<FirebaseChildEvent> observeChildAdded(Query ref) {
    return observeChildEvent(ref).filter(filterChildEvent(EventType.ADDED));
  }

  /**
   * Creates an observable only for the child changed method
   *
   * @param ref {@link Query} this is reference of a Firebase Query
   * @return an {@link rx.Observable} of {@link FirebaseChildEvent}
   * to use
   */
  public Observable<FirebaseChildEvent> observeChildChanged(Query ref) {
    return observeChildEvent(ref).filter(filterChildEvent(EventType.CHANGED));
  }

  /**
   * Creates an observable only for the child removed method
   *
   * @param ref {@link Query} this is reference of a Firebase Query
   * @return an {@link rx.Observable} of {@link FirebaseChildEvent}
   * to use
   */
  public Observable<FirebaseChildEvent> observeChildRemoved(Query ref) {
    return observeChildEvent(ref).filter(filterChildEvent(EventType.REMOVED));
  }

  /**
   * Creates an observable only for the child removed method
   *
   * @param ref {@link Query} this is reference of a Firebase Query
   * @return an {@link rx.Observable} of {@link FirebaseChildEvent}
   * to use
   */
  public Observable<FirebaseChildEvent> observeChildMoved(Query ref) {
    return observeChildEvent(ref).filter(filterChildEvent(EventType.MOVED));
  }

  /**
   * Functions which filters a stream of {@link rx.Observable} according to firebase
   * child event type
   *
   * @param type {@link FirebaseChildEvent}
   * @return {@link Func1} a function which returns a boolean if the type are equals
   */
  public Func1<FirebaseChildEvent, Boolean> filterChildEvent(final EventType type) {
    return new Func1<FirebaseChildEvent, Boolean>() {
      @Override public Boolean call(FirebaseChildEvent firebaseChildEvent) {
        return firebaseChildEvent.getEventType() == type;
      }
    };
  }
}
