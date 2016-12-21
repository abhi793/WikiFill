package in.demo.wikifill;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import in.demo.wikifill.Network.GetParagraph;

public class MainActivity extends AppCompatActivity {
    private TextView title;
    Animation animation;
    MainActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        title = (TextView)findViewById(R.id.level_textview);
        animation =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        GetParagraph.getInstance().getwikiParagraph(activity); //Network call to get the random paragraph from wiki
    }

    @Override
    protected void onStart() {
        super.onStart();
        title.startAnimation(animation);
    }

    public void callBackFromGetParagraph(String [] arr)
    {

    }
}
