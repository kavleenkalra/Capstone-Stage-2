package com.example.android.eventasy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.android.eventasy.R;

/**
 * Created by kakalra on 12/9/2016.
 */

public class SplashScreen extends Activity {
    ImageView splashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        splashImageView=(ImageView)findViewById(R.id.splash_image);
        Thread timerThread=new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*The splash screen activity must not be shown when the user presses the back button.
          In order to do this, we should destroy the splash screen activity after it is shown for few seconds.
         */
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            splashImageView.setBackgroundResource(R.drawable.splash_screen_image_landscape);
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            splashImageView.setBackgroundResource(R.drawable.splash_screen_image_portrait);
    }
}
