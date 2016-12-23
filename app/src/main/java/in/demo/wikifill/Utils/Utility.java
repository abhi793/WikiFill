package in.demo.wikifill.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Abhishek Pc on 22-12-2016.
 */

public class Utility {
    private static Utility utility;
    private Utility()
    {
    }
    public static Utility getInstance()
    {
        if(utility==null)
            utility = new Utility();
        return utility;
    }
    public String [] stringManipulation(String line)
    {
    String [] ans = new String[3];
    String [] temp = line.split(" ");
    int idx = new Random().nextInt(temp.length-1);
    StringBuilder start = new StringBuilder();
    StringBuilder end = new StringBuilder();
    for(int i=0;i<idx;i++)
    {
        start.append(temp[i]+" ");
    }
    for(int i=idx+1;i<temp.length-1;i++)
    {
        end.append(temp[i]+" ");
    }
    ans[0]=start.toString();
    ans[1]=temp[idx];
    ans[2]=end.toString();
    return ans;
    }
    public ArrayList<String> shuffle(ArrayList<String> answers)
    {
        ArrayList<String> shuffledList = new ArrayList<>();
        for (String word : answers) {
            shuffledList.add(word);
            }
        for(int i = 0 ;i<=5;i++) {
            int idx = new Random().nextInt(answers.size() - 1);
            Collections.swap(shuffledList,i,idx);
        }
        return shuffledList;
    }
}