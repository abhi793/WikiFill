package in.demo.wikifill;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import in.demo.wikifill.Model.ListItemModel;
import in.demo.wikifill.Network.GetParagraph;
import in.demo.wikifill.Utils.Utility;
import in.demo.wikifill.adapter.CustomListAdapter;

public class MainActivity extends AppCompatActivity {
    private TextView title;
    Animation animation;
    MainActivity activity;
    private ArrayList<String> answers;
    private ArrayList <String> shuffledanswers;
    private ArrayList <ListItemModel> modelList;
    private ListView linesListView;
    private CustomListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        title = (TextView)findViewById(R.id.level_textview);
        animation =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        GetParagraph.getInstance().getwikiParagraph(activity); //Network call to get the random paragraph from wiki
        answers = new ArrayList<String>();
        linesListView = (ListView)findViewById(R.id.line_list);
        shuffledanswers = new ArrayList<String>();
        modelList = new ArrayList<ListItemModel>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        title.startAnimation(animation);
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
        adapter = new CustomListAdapter(this,modelList,shuffledanswers);
        linesListView.setAdapter(adapter);
    }
}
