package com.example.arkitvora.newsfeed;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/*created using Android Studio (Beta) 0.8.14
www.101apps.co.za*/

public class MainActivity extends ActionBarActivity {

    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView;
    private static ArrayList<PersonData> people;
    private static ArrayList<PersonData> person;
    static View.OnClickListener myOnClickListener;
    static Button.OnClickListener buttonOnClickListener;
    private static ArrayList<Integer> removedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        myOnClickListener = new MyOnClickListener(this);
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
        return true;
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
