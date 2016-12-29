package in.demo.wikifill.network;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import in.demo.wikifill.utils.Constants;
import in.demo.wikifill.ui.MainActivity;
import in.demo.wikifill.R;
import in.demo.wikifill.app.AppController;


public class GetParagraph {

    private static GetParagraph getParagraph;

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
    public void getWikiParagraph(final MainActivity activity)
    {

        String url = activity.getResources().getString(R.string.get_paragraph_url);
        StringRequest paragraphReq = new StringRequest(url,
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
                            int paragraphLength = lines.length;
                            String [] linesToBeShown = new String[Constants.ARRAY_SIZE];
                            /**
                             * Check whether the paragraph we have fetched is of appropriate size or not
                             * we make a recursive call until we get paragraph of at least 14 lines.
                             */
                            if(paragraphLength< Constants.PARAGRAPH_SIZE)
                            {
                                getWikiParagraph(activity);
                            }
                            else
                            {
                                int linesIndex=0;
                                for(int i =0 ;i<Constants.ARRAY_SIZE;)
                                {
                                    if(linesIndex>paragraphLength-1)
                                        break;
                                    String line = lines[linesIndex];
                                    if(line.length()>Constants.PARAGRAPH_SIZE) {
                                        linesToBeShown[i] = lines[linesIndex].trim();
                                        i++;
                                        linesIndex++;
                                    }
                                    else
                                    linesIndex++;
                                }
                                activity.callBackFromGetParagraph(linesToBeShown);
                            }
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Log.e("JsonException",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(activity,"Try Again",Toast.LENGTH_SHORT).show();
            }
        });


        AppController.getInstance().addToRequestQueue(paragraphReq);
    }
}
