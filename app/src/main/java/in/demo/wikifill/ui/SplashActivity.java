package in.demo.wikifill.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import in.demo.wikifill.R;
import in.demo.wikifill.utils.Constants;

public class SplashActivity extends Activity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        TextView welcomeTextview = (TextView)findViewById(R.id.welcome_message);
        Animation animation =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_screen_animation);
        welcomeTextview.startAnimation(animation);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, Constants.SPLASH_DISPLAY_LENGTH);
    }
}
