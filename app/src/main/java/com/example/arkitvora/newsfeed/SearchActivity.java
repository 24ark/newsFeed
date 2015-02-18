package com.example.arkitvora.newsfeed;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class SearchActivity extends BaseActivity {

    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    private static ArrayList<PersonData> searchArray;
    private static ArrayList<PersonData> person;
    static View.OnClickListener myOnClickListener1;
    static Button.OnClickListener buttonOnClickListener;
    private static ArrayList<Integer> removedItems;
    private static ArrayList<Integer> addedNewItems;
    private static Integer totalfeeds=0;
    public String searchText;
    public static String puserName;
    public static String puserEmail;
    public static String check;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);
        getLayoutInflater().inflate(R.layout.activity_search, frameLayout);



        // enable ActionBar app icon to behave as action to toggle nav drawer

//        recyclerView.isInEditMode();



        myOnClickListener1 = new MyOnClickListener1(this);
        String url = "http://192.168.1.38:8080/user_search";
        String s = MainActivity.searchText;
        getSearchDataJson(url, s);
        //buttonOnClickListener = new ButtonOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchArray = new ArrayList<PersonData>();
     /*   for (int i = 0; i < MyData.nameArray.length; i++) {
            people.add(new PersonData(
                    MyData.nameArray[i],
                    MyData.emailArray[i],
                    MyData.drawableArray[i],
                    MyData.id_[i],
                    MyData.tickcount[i]
            ));
        }*/
       // person = people;

        removedItems = new ArrayList<Integer>();

        adapter = new MyAdapterSearch(searchArray);
        recyclerView.setAdapter(adapter);
    }





    public static void getSearchDataJson(String url , String searchText) {
        url = url +"?searchuser="+searchText;
        //    final ProgressDialog pDialog = new ProgressDialog(getActivity());
        //  pDialog.setMessage("Loading...");
        //pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ds", response.toString());
                        //searchArray=new ArrayList();

                        //friendListItem.add(new ProfileBasics("Ha","ja","asas@sd",0,Boolean.FALSE));

                        for(int i=0 ; i < response.length() ; i++) {
                            try {
                                while(response == null) {
                                    Log.d("aaaaasearch","searchaaaaaa");
                                }
                                JSONObject obj = response.getJSONObject(i);
                                Log.d("getting full name:", obj.get("fullname").toString());
                                searchArray.add(new PersonData(
                                        obj.get("fullname").toString(),
                                        obj.get("email").toString(),
                                        MyData.drawableArray[0],
                                        MyData.id_[0],
                                        MyData.tickcount[0]
                                ));

                                adapter.notifyDataSetChanged();


                                //Log.d("Asddadasd" , friendListItem.get(0).toString());



                                // MainActivity.adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //  if(pDialog != null) {
                        //    pDialog.dismiss();
                        // }




                        // pDialog.hide();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("zcxzc", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });




// Adding request to request queue

        VolleySingleton.getInstance().addToRequestQueue(req);
    }



/*    public void getSearchDataJson(String url , String searchText) {
        url = url +"?searchuser="+searchText;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ds", response.toString());


                        for(int i=0 ; i < response.length() ; i++) {
                            try {
                                while(response == null) {
                                    Log.d("aaaaasearch","searchaaaaaa");
                                }
                                JSONObject obj = response.getJSONObject(i);
                                FriendItemFragment.friendListItem.add(new ProfileBasics(
                                        obj.get("fullname").toString(),
                                        "",
                                        obj.get("email").toString(),
                                        ProfileData.drawableArray[i],
                                        false
                                ));

                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(pDialog != null) {
                            pDialog.dismiss();
                        }




                        // pDialog.hide();


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
    }  */









    private  class MyOnClickListener1 implements View.OnClickListener, SearchDataFragment.OnFragmentInteractionListener{

        private final Context context;

        private MyOnClickListener1(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            String url = "http://192.168.1.38:8080/friends_check";
           // Log.d("cascsavcgdvasfhmcdvlalsbvcds" , url);
            int selectedItemPosition = recyclerView.getChildPosition(v);
            Log.d("selectedItemPosition", Integer.toString(selectedItemPosition));

            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            TextView textViewEmail
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewEmail);
            Log.d("cascsavcgdvasfhmcdvlalsbvcds" , textViewName.getText().toString());
            puserEmail = textViewEmail.getText().toString();
            puserName = textViewName.getText().toString();

            getCheckFriendData(url , "kavya@jain.com" , puserEmail);

          //  getUserDataJson(url, textViewName.getText().toString());
           // ProfileActivity profileObj = new ProfileActivity();
           startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
          //  profileObj.profileName.setText(textViewName.getText().toString());
           // profileObj.profileEmail.setText(textViewEmail.getText().toString());

        }


        public  void getUserDataJson(String url , String userName) {
            url = url +"?username="+userName;
            //    final ProgressDialog pDialog = new ProgressDialog(getActivity());
            //  pDialog.setMessage("Loading...");
            //pDialog.show();

            JsonArrayRequest req = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("ds", response.toString());
                            //searchArray=new ArrayList();

                            //friendListItem.add(new ProfileBasics("Ha","ja","asas@sd",0,Boolean.FALSE));

                            for(int i=0 ; i < response.length() ; i++) {
                                try {
                                    while(response == null) {
                                        Log.d("aaaaasearch","searchaaaaaa");
                                    }
                                    JSONObject obj = response.getJSONObject(i);
                                    Log.d("getting full name:", obj.get("fullname").toString());
                                    String firstName = obj.getJSONObject("user").get("firstname").toString();
                                    String lastName = obj.getJSONObject("user").get("lastname").toString();
                                    String fullName = firstName+" "+lastName;
                                    String email = obj.getJSONObject("user").get("email").toString();

                                    Log.d("dataaaaa" , obj.toString());

                                    adapter.notifyDataSetChanged();


                                    //Log.d("Asddadasd" , friendListItem.get(0).toString());



                                    // MainActivity.adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //  if(pDialog != null) {
                            //    pDialog.dismiss();
                            // }




                            // pDialog.hide();


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("zcxzc", "Error: " + error.getMessage());
                    //pDialog.hide();
                }
            });




// Adding request to request queue

            VolleySingleton.getInstance().addToRequestQueue(req);
        }


        public void getCheckFriendData(String url , String userName , String friendName) {
            url = url +"?userid="+userName+"&friendid="+friendName;
           // HashMap<String, String> params = new HashMap<String, String>();
            //params.put("login", userName);
            final ProgressDialog pDialog = new ProgressDialog(SearchActivity.this);
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

                                check = response.get("msg").toString();

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



        @Override
        public void onFragmentInteraction(String id) {

        }
    }











    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_new_item) {
            Toast.makeText(this, "adding", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
            startActivity(intent);


        }
        if (item.getItemId() == R.id.search_item) {
//            open a search bar
            LayoutInflater li = LayoutInflater.from(this);
            final Fragment fragment = new SearchDataFragment();
            View promptsView = li.inflate(R.layout.searchprompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text
                                    // search.setText(userInput.getText());
                                    searchText = userInput.getText().toString();
                                    String url = "http://192.168.1.38:8080/user_search";
                                    SearchDataFragment.getSearchDataJson(url, searchText);


                                    Log.d("position", Integer.toString(position));
                                    // getLayoutInflater().inflate(R.layout.fragment_frienditem, frameLayout);
                                    Log.d(userInput.getText().toString() , "this was searched for");

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();

        }


        return true;
    }

    private void addNewOneToList()
    {
        Toast.makeText(this, totalfeeds.toString(), Toast.LENGTH_SHORT).show();
        PersonData p =new PersonData("Blue","kj@blue.com",R.drawable.cute,totalfeeds,0);
        searchArray.add(p);

        totalfeeds++;


        Toast.makeText(this, "added to people", Toast.LENGTH_SHORT).show();
        adapter.notifyItemInserted(totalfeeds);


        addedNewItems.remove(0);

    }

    private void addRemovedItemToList() {
        int addItemAtListPosition = 3;
        searchArray.add(addItemAtListPosition, new PersonData(
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
