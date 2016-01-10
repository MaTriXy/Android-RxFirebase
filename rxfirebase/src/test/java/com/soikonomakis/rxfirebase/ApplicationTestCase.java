package com.soikonomakis.rxfirebase;

import android.os.Build;
import java.lang.reflect.Field;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(value = RobolectricGradleTestRunner.class)
@Config(application = ApplicationStub.class, constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public abstract class ApplicationTestCase {

  protected void resetSingleton(Class cls) throws NoSuchFieldException, IllegalAccessException {
    Field instance = cls.getDeclaredField("instance");
    instance.setAccessible(true);
    instance.set(null, null);
  }
}
