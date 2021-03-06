package com.udacity.gradle.builditbigger.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.joketelling.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.joketelling.JokeActivity;
import com.udacity.gradle.builditbigger.R;

import java.io.IOException;

/**
 * Created by gishiru on 2016/01/01.
 */
public class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
  private static final String URL_ROOT = "http://10.0.2.4:8080/_ah/api/";  // Localhost's IP address.

  private EndpointsAsyncTaskListener mListener = null;
  private MyApi mMyApi = null;

  private Context mContext = null;
  private ProgressBar mSpinner = null;

  public EndpointsAsyncTask(Activity activity) {
    super();

    if (activity != null) {
      mSpinner = (ProgressBar)activity.findViewById(R.id.progress);
    }
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();

    if (mSpinner != null) {
      mSpinner.setVisibility(View.VISIBLE);
    }
  }

  @Override
  protected String doInBackground(Context... contexts) {
    if (mMyApi == null) {
      MyApi.Builder myApiBuilder =
          new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
              .setRootUrl(URL_ROOT)

              .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                  request.setDisableGZipContent(true);  // Turn off compression when running against
                  // local devappserver.
                }
              });

      mMyApi = myApiBuilder.build();
    }

    mContext = contexts[0];

    try {
      return mMyApi.sayJoke().execute().getData();
    } catch (IOException e) {
      return e.getMessage();
    }
  }

  @Override
  protected void onPostExecute(String s) {
    super.onPostExecute(s);

    if (mSpinner != null) {
      mSpinner.setVisibility(View.GONE);
    }

    if (mListener != null) {
      mListener.onCompleted(s);
    } else {
      // Start activity and pass a joke.
      Intent intent = new Intent(mContext, JokeActivity.class);
      intent.putExtra(JokeActivity.EXTRA_KEY_JOKE, s);
      mContext.startActivity(intent);  // Calling this in android test causes runtime exception.
    }
  }

  public EndpointsAsyncTask setListener(EndpointsAsyncTaskListener listener) {
    mListener = listener;
    return this;
  }

  public interface EndpointsAsyncTaskListener {
    void onCompleted(String joke);
  }
}
