package com.example.android.xpenses.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.example.android.xpenses.DataFiles.TransactionsContract.TransactionsEntry;
import com.example.android.xpenses.R;

public class TransactionsCursorAdapter extends CursorAdapter {

    private boolean empty;
    private String whichActivity;

    public TransactionsCursorAdapter(Context context, Cursor cursor, String whichActivity) {
        super(context, cursor);
        this.whichActivity = whichActivity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.transaction_list_item_layout, parent, false);
    }

    @Override
    public int getCount() {
        int superCount = super.getCount();
        if(whichActivity.equals("home")){
            return Math.min(superCount, 3);
        } else{
            return superCount;
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView one = view.findViewById(R.id.one);
        TextView two = view.findViewById(R.id.two);
        TextView three = view.findViewById(R.id.three);
        TextView four = view.findViewById(R.id.four);

        String amount = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsEntry.COLUMN_TRANSACTION_AMOUNT));
        String account = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsEntry.COLUMN_TRANSACTION_CATEGORY));
        String fund = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsEntry.COLUMN_TRANSACTION_FUND));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsEntry.COLUMN_TRANSACTION_DATE));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsEntry.COLUMN_TRANSACTION_TYPE));
        String tag = cursor.getString(cursor.getColumnIndexOrThrow(TransactionsEntry.COLUMN_TRANSACTION_TAG));

        if("E".equals(type)) {
            one.setTextColor(Color.RED);
            two.setText(category);
            four.setText(tag);
        }
        if("I".equals(type)) {
            one.setTextColor(Color.parseColor("#85bb65"));
            two.setText(account);
            four.setText(fund);
        }
        one.setText(amount);
        three.setText(date);
    }

}
