package com.example.android.xpenses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.xpenses.DataTypes.TitleAmountObject;
import com.example.android.xpenses.R;

import java.util.ArrayList;

public class BottomListAdapter extends ArrayAdapter<TitleAmountObject> {

    public ArrayList<TitleAmountObject> bottomList;

    public BottomListAdapter(@NonNull Context context, ArrayList<TitleAmountObject> bottomList) {
        super(context, 0, bottomList);
        this.bottomList = bottomList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_listitem, parent, false);
        }

        TitleAmountObject item = getItem(position);
        TextView titletv = listItemView.findViewById(R.id.title_tv);
        TextView amount = listItemView.findViewById(R.id.amount_tv);

        titletv.setText(item.getTitle());
        amount.setText(item.getAmount());

        return listItemView;
    }
}
