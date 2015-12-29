package com.joketelling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


/**
 * Created by gishiru on 2015/12/29.
 */
public class JokeActivity extends Activity {
  public static final String EXTRA_KEY_JOKE = "joke";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    Toast.makeText(this, intent.getStringExtra(EXTRA_KEY_JOKE), Toast.LENGTH_SHORT).show();
  }
}
