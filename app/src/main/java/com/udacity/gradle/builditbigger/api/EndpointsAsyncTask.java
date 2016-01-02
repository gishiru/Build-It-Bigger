package com.udacity.gradle.builditbigger.api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.joketelling.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.joketelling.JokeActivity;

import java.io.IOException;

/**
 * Created by gishiru on 2016/01/01.
 */
public class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
  private static final String URL_ROOT = "http://10.0.2.4:8080/_ah/api/";

  private MyApi mMyApi = null;
  private Context mContext = null;

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

    Intent intent = new Intent(mContext, JokeActivity.class);
    intent.putExtra(JokeActivity.EXTRA_KEY_JOKE, s);
    mContext.startActivity(intent);

  }
}
