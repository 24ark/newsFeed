package com.example.arkitvora.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kavyajain on 17/02/15.
 */
public class FriendAdapter extends ArrayAdapter{
    private Context context;
    private boolean useList = true;
    private Button buttonAdd;
    public FriendAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_2, items);
        this.context = context;
    }
    /** * Holder for the list items. */
    private class ViewHolder{ TextView nameText; TextView friendFlag; Button Add;}
    /** * * @param position * @param convertView * @param parent * @return */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ProfileBasics item = (ProfileBasics)getItem(position);
        View viewToUse = null;
        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        { if(useList)
        {
            viewToUse = mInflater.inflate(R.layout.fragment_frienditem_list, null);
        }
        else
        {
            viewToUse = mInflater.inflate(R.layout.fragment_frienditem_grid, null);
        }
            holder = new ViewHolder();
            holder.nameText = (TextView)viewToUse.findViewById(R.id.nameView);
            holder.friendFlag=(TextView)viewToUse.findViewById(R.id.friendship);
            holder.Add=(Button)viewToUse.findViewById(R.id.Add);
            viewToUse.setTag(holder);
        }
        else
        {
            viewToUse = convertView; holder = (ViewHolder) viewToUse.getTag();
        }
        holder.nameText.setText(item.getFirstName()+" "+item.getLastName());
       if(item.getFriendFlag()){holder.friendFlag.setText("Friends"); holder.Add.setVisibility(View.INVISIBLE);} else {holder.friendFlag.setText("Not Friends.");}


        holder.Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("outerObject","Add request sent");
                item.changeFriendFlag();


            }
        });
         return viewToUse;
    }
}
