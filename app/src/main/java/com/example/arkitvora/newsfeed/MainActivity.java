package com.example.arkitvora.newsfeed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


/*created using Android Studio (Beta) 0.8.14
www.101apps.co.za*/

public class MainActivity extends BaseActivity {

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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.feed_activity);
        getLayoutInflater().inflate(R.layout.feed_activity, frameLayout);



        // enable ActionBar app icon to behave as action to toggle nav drawer



        myOnClickListener = new MyOnClickListener(this);
        String url = "http://192.168.1.224:8080/tweet_get";
        getFeedDataJson(url, "fdfd");
        //buttonOnClickListener = new ButtonOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        people = new ArrayList<PersonData>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            people.add(new PersonData(
                    MyData.nameArray[i],
                    MyData.emailArray[i],
                    MyData.drawableArray[i],
                    MyData.id_[i],
                    MyData.tickcount[i]
            ));
        }
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
                            Log.d("insidetry" , "insidetry");
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


    public void getFeedDataJson(String url , String userName) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("dsdsf", response.toString());
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









    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View v) {
            Log.d("outerObject", v.toString());
            int selectedItemPosition = recyclerView.getChildPosition(v);
            Log.d("selectedItemPosition", Integer.toString(selectedItemPosition));
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();
            Log.d("selectedItemText", selectedName);
            int selectedItemId = -1;
            for (int i = 0; i < MyData.nameArray.length; i++) {
                if (selectedName.equals(MyData.nameArray[i])) {
                    selectedItemId = MyData.id_[i];
                }
            }
            removedItems.add(selectedItemId);
            people.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);

        }
    }

    public void incrementLikeCount(View v) {
        int selectedItemPosition = recyclerView.getChildPosition(v);
        //Log.d("selectedItemPosition", Integer.toString(selectedItemPosition));
        RecyclerView.ViewHolder viewHolder
                = recyclerView.findViewHolderForPosition(selectedItemPosition);
        TextView textViewName
                = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
        String selectedName = (String) textViewName.getText();

        int selectedItemId = -1;
        for (int i = 0; i < MyData.nameArray.length; i++) {
            if (selectedName.equals(MyData.nameArray[i])) {
                selectedItemId = MyData.id_[i];
            }
        }
        MyData.tickcount[selectedItemId]++;
       // Log.d("selectedItemText", selectedName);
     //   Log.d("selectedItemLikeCount", Integer.toString(MyData.tickcount[selectedItemId]));
        people.remove(selectedItemPosition);
        people.add(selectedItemPosition, new PersonData(
                MyData.nameArray[selectedItemId],
                MyData.emailArray[selectedItemId],
                MyData.drawableArray[selectedItemId],
                MyData.id_[selectedItemId],
                MyData.tickcount[selectedItemId]
        ));
      //  PersonData p = people.get(selectedItemId);
       // people.remove(4);
      //  people.add(selectedItemPosition,p);
       // adapter.notifyItemChanged(selectedItemId);


      //  adapter = new MyAdapter(people);
       // adapter.notifyDataSetChanged();

      //  adapter.notifyItemChanged(selectedItemPosition);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                    adapter.notifyDataSetChanged();

            }
        });



    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_add_item) {
//            check if any items to add
            if (removedItems.size() != 0) {
                addRemovedItemToList();
            } else {
                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
            }
        }
        else if(item.getItemId()==R.id.action_new_item) {
            Toast.makeText(this, "adding", Toast.LENGTH_SHORT).show();

            addedNewItems.add(totalfeeds);


            if (addedNewItems.size() != 0) {
                addNewOneToList();
            } else {
                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    private void addNewOneToList()
    {
        Toast.makeText(this, totalfeeds.toString(), Toast.LENGTH_SHORT).show();
        PersonData p =new PersonData("Blue","kj@blue.com",R.drawable.cute,totalfeeds,0);
        people.add(p);

        totalfeeds++;


        Toast.makeText(this, "added to people", Toast.LENGTH_SHORT).show();
        adapter.notifyItemInserted(totalfeeds);


        addedNewItems.remove(0);

    }

    private void addRemovedItemToList() {
        int addItemAtListPosition = 3;
        people.add(addItemAtListPosition, new PersonData(
                MyData.nameArray[removedItems.get(0)],
                MyData.emailArray[removedItems.get(0)],
                MyData.drawableArray[removedItems.get(0)],
                MyData.id_[removedItems.get(0)],
                MyData.tickcount[removedItems.get(0)]
        ));
        adapter.notifyItemInserted(addItemAtListPosition);
        removedItems.remove(0);
    }









}
