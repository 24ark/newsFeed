package com.example.arkitvora.newsfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class NewPostActivity extends ActionBarActivity {


    public EditText tweetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        tweetData = (EditText) findViewById(R.id.tweet_data);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_post) {
//            check if any items to add
            if (tweetData.length() > 0) {
                String SERVER_URL ="http://192.168.1.38:8080/tweet_post";
                String userName = LoginActivity.myUserName;
                String tweetdata = tweetData.getText().toString();
                postTweetData(SERVER_URL , userName , tweetdata );

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Nothing to Post", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void postTweetData(String url , String userName , String tweetData) {

        HashMap<String, String> params = new HashMap<String, String>();
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        params.put("username", userName);
        params.put("tweetbd" , tweetData);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url , new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("volleyres", response.toString());
                            if(pDialog != null) {
                                pDialog.dismiss();
                            }
                            if(response.get("msg").toString().equals("1")) {
                                Intent intent = new Intent(NewPostActivity.this, MainActivity.class);


                                startActivity(intent);
                            }
                             else {
                                Toast.makeText(getApplicationContext(), "Cant connect now", Toast.LENGTH_LONG).show();
                            }
                            //pDialog.hide();
                            //Log.d("volleyres" , response.get("msg").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                VolleyLog.e("Error: ", error.getMessage());
            }
        });


        VolleySingleton.getInstance().addToRequestQueue(req);

    }

}
