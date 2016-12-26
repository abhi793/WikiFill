package in.demo.wikifill.Network;

import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import in.demo.wikifill.ui.MainActivity;
import in.demo.wikifill.R;
import in.demo.wikifill.app.AppController;

/**
 * Created by Abhishek Pc on 21-12-2016.
 */

public class GetParagraph {

     static GetParagraph getParagraph;

     private GetParagraph()
     {

     }
    public static GetParagraph getInstance()
    {
        if(getParagraph==null)
        {
            getParagraph = new GetParagraph();
        }
        return getParagraph;
    }
    public void getwikiParagraph(final MainActivity activity)
    {

        String url = activity.getResources().getString(R.string.get_paragraph_url);
        StringRequest vehiclesReq = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject query = jsonObject.getJSONObject("query");
                            JSONObject pages = query.getJSONObject("pages");
                            Iterator<?> keys = pages.keys();//Logic to get the first object from a jsonObject
                            String key = (String)keys.next();
                            JSONObject content = pages.getJSONObject(key);
                            String paragraph = content.getString("extract").replaceAll("\\<[^>]*>","").replaceAll("\\\n","").replaceAll("\\/","");
                            String [] lines = paragraph.split("\\.");
                            int temp = lines.length;
                            String [] linesToBeShown = new String[10];
                            //Check whether the paragraph we have fetched is of approprite size or not
                            //we make a recursive call until we get paragarph of atleast 14 lines.
                            if(temp<15)
                            {
                                getwikiParagraph(activity);
                            }
                            else
                            {
                                int j=0;
                                for(int i =0 ;i<10;)
                                {
                                    if(j>temp-1)
                                        break;
                                    String line = lines[j];
                                    if(line.length()>15) {
                                        linesToBeShown[i] = lines[j].trim();
                                        i++;
                                        j++;
                                    }
                                    else
                                    j++;
                                }
                                activity.callBackFromGetParagraph(linesToBeShown);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(activity,"Try Again",Toast.LENGTH_SHORT).show();
            }
        });


        AppController.getInstance().addToRequestQueue(vehiclesReq);
    }
}
