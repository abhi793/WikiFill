package in.demo.wikifill.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import in.demo.wikifill.Model.ListItemModel;
import in.demo.wikifill.Network.GetParagraph;
import in.demo.wikifill.R;
import in.demo.wikifill.Utils.Utility;
import in.demo.wikifill.adapter.CustomListAdapter;

public class MainActivity extends AppCompatActivity {
    private TextView title;
    Animation animation;
    Animation animation1;
    MainActivity activity;
    private ArrayList<String> answers;
    private ArrayList <String> shuffledanswers;
    private ArrayList <ListItemModel> modelList;
    private ListView linesListView;
    private CustomListAdapter adapter;
    private ProgressDialog progressDialog;
    private Button submitButton;
    public String currentLevel;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String [] levelUparray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        levelUparray = getResources().getStringArray(R.array.random_words);//extra words to add in shuffled list on level up
        title = (TextView)findViewById(R.id.level_textview);
        submitButton = (Button)findViewById(R.id.submit_button);
        animation =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        answers = new ArrayList<String>();
        linesListView = (ListView)findViewById(R.id.line_list);
        shuffledanswers = new ArrayList<String>();
        modelList = new ArrayList<ListItemModel>();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        currentLevel = pref.getString("currentLevel", null);
        if (currentLevel != null && !currentLevel.isEmpty()) {
            title.setText("LEVEL "+ currentLevel);
        }
        else
        {
            currentLevel ="1";
            editor.putString("currentLevel","1");
            editor.commit();
        }

       networkCall();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count =0;
                for(int i = 0;i<10;i++)
                {
                    String A = answers.get(i).toString().trim();
                    String B = modelList.get(i).getBlank().toString().trim();
                    if(A.equals(B))
                        count++;
                }
                showResult(count);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void callBackFromGetParagraph(String [] arr)
    {

        for(int i =0;i<arr.length;i++)
        {
            String [] temp = Utility.getInstance().stringManipulation(arr[i]);
            ListItemModel model = new ListItemModel(temp[0],temp[2]);
            modelList.add(model);
            answers.add(temp[1]);
        }
        shuffledanswers = Utility.getInstance().shuffle(answers);
        int levelUp=Integer.parseInt(currentLevel)-1;
        if(levelUp>0)
        {
            int i = 0;
            while(i<levelUp)
            {
                shuffledanswers.add(levelUparray[i]); //adding random words on level up
                i++;
            }
        }
        progressDialog.dismiss();
        adapter = new CustomListAdapter(this,modelList,shuffledanswers);
        linesListView.setAdapter(adapter);
        title.startAnimation(animation);
        linesListView.startAnimation(animation1);
    }
    public void showResult(final int score)
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.scorecard_dialogbox,
                (ViewGroup) findViewById(R.id.dialog_layout));
        TextView messageTextview =(TextView)layout.findViewById(R.id.message);
        if(score>5)
            messageTextview.setText(getString(R.string.score_message_pass));
        else
            messageTextview.setText(getString(R.string.score_message_fail));
        TextView scoreTextview = (TextView)layout.findViewById(R.id.score);
        scoreTextview.setText(getString(R.string.your_score)+" "+String.valueOf(score)+"/10");
        dialog.setView(layout);
        dialog.setTitle("Scorecard");
        if(score>5) {
            dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    int levelVal=Integer.parseInt(currentLevel);
                    if(levelVal==5)
                    {
                        Intent intent = getIntent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setClass(MainActivity.this, EndActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        currentLevel = String.valueOf(levelVal + 1);
                        editor.putString("currentLevel", String.valueOf(levelVal + 1));
                        editor.commit();
                        title.setText("LEVEL " + String.valueOf(levelVal + 1));
                        modelList.clear();
                        answers.clear();
                        shuffledanswers.clear();
                        networkCall();
                        dialog.dismiss();
                    }
                }

            });
        }
        else {
            dialog.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    modelList.clear();
                    answers.clear();
                    shuffledanswers.clear();
                    networkCall();
                }
            });
        }
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }
    public void networkCall()
    {
        if(Utility.getInstance().isInternetConnected(this))
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetching Paragraph with blanks...");
            progressDialog.show();
            GetParagraph.getInstance().getwikiParagraph(activity); //Network call to get the random paragraph from wiki
        }
        else
            buildAlertMessageNoInternet();
    }
    private void buildAlertMessageNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your internet seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                    //
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
