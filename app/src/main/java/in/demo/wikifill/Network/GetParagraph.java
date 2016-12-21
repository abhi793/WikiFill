package in.demo.wikifill.Network;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.demo.wikifill.MainActivity;
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
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Downloading...");
        progressDialog.show();
        StringRequest vehiclesReq = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(activity,"Try Again",Toast.LENGTH_SHORT).show();
            }
        });


        AppController.getInstance().addToRequestQueue(vehiclesReq);
    }
}
