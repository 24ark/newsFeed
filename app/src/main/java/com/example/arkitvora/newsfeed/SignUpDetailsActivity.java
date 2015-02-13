package com.example.arkitvora.newsfeed;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class SignUpDetailsActivity extends BaseActivity {


    public static EditText firstName;
    public static EditText lastName;
    public static EditText userEmail;
    public static EditText password;
    public static EditText confirmPassword;
    public static ProgressBar pb;
    public static Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_up_details);
        getLayoutInflater().inflate(R.layout.activity_sign_up_details, frameLayout);

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        userEmail = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);

        pb=(ProgressBar)findViewById(R.id.pb);
        pb.setVisibility(View.GONE);



        signUpButton = (Button) findViewById(R.id.signup_button);




    /*    final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setVisibility(View.VISIBLE);
                            }
                        });

         //               pb.setVisibility(View.VISIBLE);
                        String userName = firstName.getText().toString();
                        String email = userEmail.getText().toString();
                        String password = lastName.getText().toString();
                        LoginActivity.buttonType = "signup";
                        String s;
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://192.168.1.143:3000/signup");

                        try {
                            // Add your data

                            JSONObject json = new JSONObject();
                            json.put("username" , userName);
                            json.put("email" , email);
                            json.put("password" , password);

                            StringEntity se = new StringEntity( json.toString());
                            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                            httppost.setEntity(se);

                            // Execute HTTP Post Request
                            HttpResponse response = httpclient.execute(httppost);

                            s = EntityUtils.toString(response.getEntity());

                            s = s.toString();


                            int a=response.getStatusLine().getStatusCode();



                        } catch (ClientProtocolException e) {
                            // TODO Auto-generated catch block
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //handler.post(r);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        */

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstName.getText().toString().length()<1){

                    // out of range
                    Toast.makeText(SignUpDetailsActivity.this, "please enter something", Toast.LENGTH_LONG).show();
                }else{

                    if(userEmail.getText().toString().length()<1){

                        // out of range
                        Toast.makeText(SignUpDetailsActivity.this, "please enter something", Toast.LENGTH_LONG).show();
                    }else{
                        String SERVER_URL = "http://192.168.1.224:3000/signup";
                        String email = userEmail.getText().toString();
                        String first_name = firstName.getText().toString();
                        String last_name = lastName.getText().toString();
                        String userPassword = password.getText().toString();
                        postSignupData(SERVER_URL,email , first_name , last_name , userPassword );
                    }

                }
            }
        });



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void postSignupData(String url , String email, String firstName , String lastName , String password) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("firstname", firstName);
        params.put("lastname" , lastName);
        params.put("email" , email);
        params.put("password" , password);

        JsonObjectRequest req = new JsonObjectRequest(url , new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("volleyres" , response.toString());
                            Log.d("volleyres" , response.get("msg").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });


        VolleySingleton.getInstance().addToRequestQueue(req);

    }



    public class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        public String s;
        HttpClient httpclient = new DefaultHttpClient();

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub


                postSignUpData(params[0], params[1], params[2]);

            return null;
        }

        protected void onPostExecute(Double result){
            pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(SignUpDetailsActivity.this, ProfileActivity.class);
            // MyAsyncTask newObj = new MyAsyncTask();
            Boolean w = s == "ok";
            Log.e("check" , w.toString());
            if(s.equals("ok")) {
                startActivity(intent);
            }
            else {
                Toast.makeText(SignUpDetailsActivity.this, "Wrong Username or Password", Toast.LENGTH_LONG).show();
            }

        }
        protected void onProgressUpdate(Integer... progress){
            pb.setProgress(progress[0]);
        }



        public void postSignUpData(String userName, String email , String password) {
            // Create a new HttpClient and Post Header

            HttpPost httppost = new HttpPost("http://192.168.1.224:3000/signup");

            try {
                // Add your data

                JSONObject json = new JSONObject();
                json.put("username" , userName);
                json.put("email" , email);
                json.put("password" , password);

                StringEntity se = new StringEntity( json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                s = EntityUtils.toString(response.getEntity());

                s = s.toString();


                int a=response.getStatusLine().getStatusCode();



            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
