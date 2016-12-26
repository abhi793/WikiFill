package in.demo.wikifill.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        title = (TextView)findViewById(R.id.level_textview);
        submitButton = (Button)findViewById(R.id.submit_button);
        animation =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        answers = new ArrayList<String>();
        linesListView = (ListView)findViewById(R.id.line_list);
        shuffledanswers = new ArrayList<String>();
        modelList = new ArrayList<ListItemModel>();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Fetching Paragraph with blanks...");
        progressDialog.show();
        GetParagraph.getInstance().getwikiParagraph(activity); //Network call to get the random paragraph from wiki
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
        progressDialog.dismiss();
        adapter = new CustomListAdapter(this,modelList,shuffledanswers);
        linesListView.setAdapter(adapter);
        title.startAnimation(animation);
        linesListView.startAnimation(animation1);
    }
    public void showResult(final int score)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
        dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                if(score<=5)
                    Toast.makeText(MainActivity.this,"You have get more than 5 points to move to next level",Toast.LENGTH_LONG).show();
                else
                dialog.dismiss();
            }

        });
        dialog.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.create();
        dialog.show();
    }
}
