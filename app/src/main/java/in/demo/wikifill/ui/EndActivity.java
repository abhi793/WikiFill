package in.demo.wikifill.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.demo.wikifill.R;

public class EndActivity extends AppCompatActivity {
    private TextView endMessage;
    private Button restartButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        endMessage = (TextView)findViewById(R.id.end_message);
        restartButton = (Button)findViewById(R.id.restart_button);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("currentLevel"); // removing the level so that the game starts from level 1
                editor.commit();
                Intent intent = getIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setClass(EndActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
