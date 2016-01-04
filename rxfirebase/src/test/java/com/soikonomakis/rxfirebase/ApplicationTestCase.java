package com.soikonomakis.rxfirebase;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(value = RobolectricGradleTestRunner.class)
@Config(application = ApplicationStub.class, constants = BuildConfig.class, sdk = 21)
public abstract class ApplicationTestCase {
}
