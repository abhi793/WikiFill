package in.demo.wikifill.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    /**
     * Function to divide a sentence
     * @param line, A sentence
     * @return array of string containg the start,end and blank of the sentence
     */
    public String [] stringManipulation(String line)
    {
    String [] ans = new String[3];
    String [] temp = line.split(" ");
        int idx = temp.length/2;   //choose a random number to get the position of the string which will be blank in the sentece
        while (temp[idx].length()<=1)
        idx = new Random().nextInt(temp.length-1);// choosing the blank word of atleast 2 letters
    StringBuilder start = new StringBuilder();
    StringBuilder end = new StringBuilder();
    for(int i=0;i<idx;i++)
    {
        if(i<idx-1) {
            String getWord = temp[i] + " ";
            start.append(getWord);
        }
        else
            start.append(temp[i]);
    }
    for(int i=idx+1;i<temp.length;i++)
    {
        if(i<temp.length-1) {
            String getWord = temp[i] + " ";
            end.append(getWord);
        }
        else {
            String getWord = temp[i] + ".";
            end.append(getWord);
        }
    }
    ans[0]=start.toString();
    ans[1]=temp[idx];
    ans[2]=end.toString();
    return ans;
    }

    /**
     * shuffle the arraylist of strings
     * @param answers ,arraylist of strings
     * @return  shuffled arraylist of strings
     */
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
    public  boolean isInternetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.getState() != NetworkInfo.State.CONNECTED) {
            return false;
        }
        return true;
    }
}
