package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.udacity.gradle.builditbigger.api.EndpointsAsyncTask;

import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
  private static final int COUNT_LATCH = 1;

  private String mJoke = "";
  private CountDownLatch mSignal = null;
  private EndpointsAsyncTask mTask = null;

  public ApplicationTest() {
    super(Application.class);
  }

  @Override
  protected void setUp() throws Exception {
    mSignal = new CountDownLatch(COUNT_LATCH);  // Allows main thread to wait until a set of
                                                // operations being performed in background thread.
    mTask = new EndpointsAsyncTask(null);
  }

  @Override
  protected void tearDown() throws Exception {
    mSignal.countDown();  // Stop the blocking.
  }

  public void testGetJokeFromGce() throws InterruptedException {
    // Set up task.
    mTask.execute(getContext());
    mTask.setListener(new EndpointsAsyncTask.EndpointsAsyncTaskListener() {
      @Override
      public void onCompleted(String joke) {
        mJoke = joke;
        mSignal.countDown();  // Stop the blocking.
      }
    });

    mSignal.await();  // Start wait after start background task.

    assertEquals("This is a joke!", mJoke);
  }
}
