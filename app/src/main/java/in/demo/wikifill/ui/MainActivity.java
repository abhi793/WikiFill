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

import in.demo.wikifill.model.ListItemModel;
import in.demo.wikifill.network.GetParagraph;
import in.demo.wikifill.R;
import in.demo.wikifill.utils.Constants;
import in.demo.wikifill.utils.Utility;
import in.demo.wikifill.adapter.CustomListAdapter;

public class MainActivity extends AppCompatActivity {
    private TextView title;
    Animation animation;
    Animation animation1;
    MainActivity activity;
    private ArrayList<String> answers;
    private ArrayList <String> shuffledAnswers;
    private ArrayList <ListItemModel> modelList;
    private ListView linesListView;
    private ProgressDialog progressDialog;
    public String currentLevel;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String [] levelUpArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        levelUpArray = getResources().getStringArray(R.array.random_words);//extra words to add in shuffled list on level up
        title = (TextView)findViewById(R.id.level_textview);
        Button submitButton = (Button)findViewById(R.id.submit_button);
        animation =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        answers = new ArrayList<>();
        linesListView = (ListView)findViewById(R.id.line_list);
        shuffledAnswers = new ArrayList<>();
        modelList = new ArrayList<>();
        pref = getApplicationContext().getSharedPreferences(getString(R.string.sharedpreference_name), 0); // 0 - for private mode
        editor = pref.edit();
        currentLevel = pref.getString(getString(R.string.sharedpreference_key), null);
        if (currentLevel != null && !currentLevel.isEmpty()) { //check the current level of the user
            String textToSet = getResources().getString(R.string.level) +" "+ currentLevel;
            title.setText(textToSet);
        }
        else
        {
            currentLevel =getString(R.string.first_level);
            editor.putString(getString(R.string.sharedpreference_key),currentLevel);
            editor.commit();
        }

       networkCall();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int correctAnswersCount =0;
                for(int i = 0;i<Constants.ARRAY_SIZE;i++)
                {
                    String A = answers.get(i).trim();
                    String B = modelList.get(i).getBlank().trim();
                    if(A.equals(B))
                        correctAnswersCount++;
                }
                showResult(correctAnswersCount);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * This function sets the sentences to the List item model after dividing the sentence
     * @param arr,array of sentences fetched from wikipedia
     */
    public void callBackFromGetParagraph(String [] arr)
    {

        for(String sentence : arr)
        {
            String [] sentenceDivisions = Utility.getInstance().stringManipulation(sentence);
            ListItemModel model = new ListItemModel(sentenceDivisions[0],sentenceDivisions[2]);
            modelList.add(model);
            answers.add(sentenceDivisions[1]);
        }
        shuffledAnswers = Utility.getInstance().shuffle(answers);
        int levelUp=Integer.parseInt(currentLevel)-1;
        if(levelUp>0)
        {
            int i = 0;
            while(i<levelUp)
            {
                shuffledAnswers.add(levelUpArray[i]); //adding random words on level up
                i++;
            }
        }
        progressDialog.dismiss();
        CustomListAdapter adapter = new CustomListAdapter(this,modelList, shuffledAnswers);
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
        TextView messageTextView =(TextView)layout.findViewById(R.id.message);
        if(score>=Constants.PASSING_SCORE)
            messageTextView.setText(getString(R.string.score_message_pass));
        else
            messageTextView.setText(getString(R.string.score_message_fail));
        TextView scoreTextView = (TextView)layout.findViewById(R.id.score);
        String scoreValue = getString(R.string.your_score)+" "+String.valueOf(score)+ getString(R.string.total_score);
        scoreTextView.setText(scoreValue);
        dialog.setView(layout);
        dialog.setTitle(getString(R.string.alertdialog_title));

        if(score>= Constants.PASSING_SCORE) {
            dialog.setPositiveButton(getString(R.string.alertdialog_positive_text), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    int levelVal=Integer.parseInt(currentLevel);
                    if(levelVal==Constants.LAST_LEVEL)
                    {
                        modelList.clear();
                        answers.clear();
                        shuffledAnswers.clear();
                        Intent intent = getIntent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setClass(MainActivity.this, EndActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        currentLevel = String.valueOf(levelVal + 1);
                        editor.putString(getString(R.string.sharedpreference_key), String.valueOf(levelVal + 1));
                        editor.commit();
                        String textToSet = getResources().getString(R.string.level)+" "+String.valueOf(levelVal + 1);
                        title.setText(textToSet);
                        modelList.clear();
                        answers.clear();
                        shuffledAnswers.clear();
                        networkCall();
                        dialog.dismiss();
                    }
                }

            });
        }
        else {
            dialog.setNegativeButton(getString(R.string.alertdialog_negative_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    modelList.clear();
                    answers.clear();
                    shuffledAnswers.clear();
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
            progressDialog.setMessage(getString(R.string.progressdialog_message));
            progressDialog.show();
            GetParagraph.getInstance().getwikiParagraph(activity); //Network call to get the random paragraph from wiki
        }
        else
            buildAlertMessageNoInternet();
    }
    private void buildAlertMessageNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.internet_off_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.internet_alertdialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                    //
                })
                .setNegativeButton(getString(R.string.internet_alertdialog_negative), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
