package com.example.arkitvora.newsfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ProfileActivity extends BaseActivity {
    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_PICTURE =2;

    private String selectedImagePath;
    private ImageView img;
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    private static ArrayList<PersonData> people;
    private static ArrayList<PersonData> person;
    static View.OnClickListener myOnClickListener;
    static Button.OnClickListener buttonOnClickListener;
    private static ArrayList<Integer> removedItems;
    private static ArrayList<Integer> addedNewItems;
    private static Integer totalfeeds=0;
    public static TextView profileName;
    public static TextView profileEmail;
    public static Button addfriend;
    public static String whoseprofile;
    public static String theiremail;
    public static Button viewfriendlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.general_profile, frameLayout);
        String url = "http://192.168.1.38:8080/tweet_get";
        profileName = (TextView)findViewById(R.id.fullname);
        profileEmail = (TextView)findViewById(R.id.email);
        addfriend=(Button)findViewById(R.id.addfriend);
        viewfriendlist=(Button)findViewById(R.id.viewfriends);
        //for own profile
        if(SearchActivity.check.equals("2")){
           // Log.d("myusername",LoginActivity.myUserFullName);
            profileEmail.setText(LoginActivity.myUserName);
            profileName.setText(LoginActivity.myUserFullName);
        getFeedDataJson(url, LoginActivity.myUserName);

            addfriend.setVisibility(View.INVISIBLE);

        }
        else {
            profileEmail.setText(SearchActivity.puserEmail);
            profileName.setText(SearchActivity.puserName);
        }
        if(SearchActivity.check.equals("1")) {
            getFeedDataJson(url, SearchActivity.puserEmail);
            addfriend.setVisibility(View.INVISIBLE);
        }

        if(profileEmail.getText().toString().equals(LoginActivity.myUserName)) {
            addfriend.setVisibility(View.INVISIBLE);
            getFeedDataJson(url, LoginActivity.myUserName);
        }

        addfriend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postUrl = "http://192.168.1.38:8080/friends_post";
                postAddFriend(postUrl , LoginActivity.myUserFullName,LoginActivity.myUserName, SearchActivity.puserName,SearchActivity.puserEmail);
            }
        });
        viewfriendlist.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(profileEmail.equals(LoginActivity.myUserName))){SearchActivity.check="1";}
                String postUrl = "http://192.168.1.38:8080/friends_get";
                startActivity(new Intent(ProfileActivity.this , FriendsActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        people = new ArrayList<PersonData>();
     /*   for (int i = 0; i < MyData.nameArray.length; i++) {
            people.add(new PersonData(
                    MyData.nameArray[i],
                    MyData.emailArray[i],
                    MyData.drawableArray[i],
                    MyData.id_[i],
                    MyData.tickcount[i]
            ));
        }*/
        person = people;

        removedItems = new ArrayList<Integer>();

        adapter = new MyAdapter(people);
        recyclerView.setAdapter(adapter);
    }


    public void getFeedData(String url , String userName) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("login", userName);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // params.put("password" , password);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("insidetry", "insidetry");
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("volleyres" , response.toString());
                            pDialog.hide();

                            //Log.d("volleyres" , response.get("msg").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorin" , Integer.toString(error.networkResponse.statusCode));

                Log.d("insideerr" , "insideerr");
                VolleyLog.e("Error: ", error.getMessage());
            }
        });


        VolleySingleton.getInstance().addToRequestQueue(req);

    }


    public void postAddFriend(String url , String userName , String userid ,String friendname , final String friendid ) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("userid", userid);
        params.put("friendname", friendname);
        params.put("friendid", friendid);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // params.put("password" , password);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST ,url , new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("insidetry", "insidetry");
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("volleyres" , response.toString());
                            pDialog.hide();

                            if(response.get("msg").equals("1")) {

                                Log.d("added","friend"+friendid);
                                //startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));

                            }
                            else if(response.get("msg").equals("0")) {

                                Log.d("failed","friend"+friendid);


                            }

                            //Log.d("volleyres" , response.get("msg").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorin" , Integer.toString(error.networkResponse.statusCode));

                Log.d("insideerr" , "insideerr");
                VolleyLog.e("Error: ", error.getMessage());
            }
        });


        VolleySingleton.getInstance().addToRequestQueue(req);

    }


    public void getFeedDataString(String url , String userName) {


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("xasxxasxsx", response.toString());
                pDialog.hide();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("fdsfsdv", "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

// Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(strReq);

    }


    public void getFeedDataJson(String url , final String userName) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //JsonArrayRequest as=new JsonArrayRequest();
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("dsdshhhhhhhhf", response.toString());
                        for(int i=0 ; i < response.length() ; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                if(obj.get("username").toString().matches(userName))
                                    people.add(new PersonData(
                                            obj.get("username").toString(),
                                            obj.get("tbody").toString(),
                                            MyData.drawableArray[0],
                                            MyData.id_[0],
                                            MyData.tickcount[0]
                                    ));

                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }




                        pDialog.hide();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("zcxzc", "Error: " + error.getMessage());
                pDialog.hide();
            }
        });




// Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(req);
    }
       /* img = (ImageView)findViewById(R.id.dp);

        ((Button) findViewById(R.id.galleryButton))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
                    }
                });
        ((Button) findViewById(R.id.camButton))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {

                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, TAKE_PICTURE);

                    }
                });*/



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                img.setImageURI(selectedImageUri);
            }
        } else if (requestCode==TAKE_PICTURE){}
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}



