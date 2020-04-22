package com.example.android.xpenses;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.xpenses.Adapters.AccountListAdapter;
import com.example.android.xpenses.DataFiles.TransactionsContract;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private Button addBTn;
    private ImageView saveBtn;
    private LinearLayout ll;
    private EditText addet;
    AccountListAdapter adapter;
    ArrayList<String> accountList;


    String asdf = "add";
    String oldAcc = "";
    String newAcc = "";
    int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        addBTn = findViewById(R.id.add_btn);
        addet = findViewById(R.id.add_et);
        saveBtn = findViewById(R.id.save_btn);
        ll = findViewById(R.id.ll);


        addBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asdf = "add";
                ll.setVisibility(View.VISIBLE);
                addBTn.setVisibility(View.GONE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asdf.equals("add")) {
                    saveNewAccount();
                } else{
                    editAccount();
                }
                ll.setVisibility(View.GONE);
                addBTn.setVisibility(View.VISIBLE);

                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(addet.getWindowToken(), 0);
            }
        });
/*
        addet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    saveNewAccount();
                    ll.setVisibility(View.GONE);
                    addBTn.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
*/


        loadAccountData();

        RecyclerView accountlv = findViewById(R.id.account_lv);
        accountlv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new AccountListAdapter(accountList);
        accountlv.setLayoutManager(layoutManager);
        accountlv.setAdapter(adapter);

        adapter.setOnItemCLickListener(new AccountListAdapter.onItemCLickListener() {
            @Override
            public void onEditClick(int position) {
                ll.setVisibility(View.VISIBLE);
                addBTn.setVisibility(View.GONE);
                pos = position;
                oldAcc = accountList.get(position);
                addet.setText(oldAcc);
                asdf = "edit";
                editAccount();

            }

            @Override
            public void onDeleteClick(final int position) {

                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String selection = TransactionsContract.AccountsEntry.COLUMN_ACCOUNT + "=?";
                                String[] selectionArgs = {accountList.get(position)};

                                accountList.remove(position);
                                adapter.notifyItemRemoved(position);

                                getContentResolver().delete(TransactionsContract.AccountsEntry.CONTENT_URI_ACCOUNTS, selection, selectionArgs);
                                getContentResolver().delete(TransactionsContract.FundEntry.CONTENT_URI_FUND, selection, selectionArgs);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.mipmap.ic_launcher)
                        .show();

            }
        });
    }

    private void saveNewAccount(){
        String acc = addet.getText().toString();
        addet.setText("");
        if(!(acc.equals("")) && !(accountList.contains(acc))) {
            accountList.add(accountList.size(), acc);
            adapter.notifyItemInserted(accountList.size());

            ContentValues values = new ContentValues();
            values.put(TransactionsContract.AccountsEntry.COLUMN_ACCOUNT, acc);
            Uri u = getContentResolver().insert(TransactionsContract.AccountsEntry.CONTENT_URI_ACCOUNTS, values);

        }
    }

    private void editAccount(){
        newAcc = addet.getText().toString();
        addet.setText("");
        if(!(newAcc.equals("")) && !(accountList.contains(newAcc))) {
            ContentValues values = new ContentValues();
            values.put(TransactionsContract.AccountsEntry.COLUMN_ACCOUNT, newAcc);
            String selection = TransactionsContract.AccountsEntry.COLUMN_ACCOUNT + "=?";
            String[] selectionArgs = {oldAcc};
            getContentResolver().update(TransactionsContract.AccountsEntry.CONTENT_URI_ACCOUNTS, values, selection, selectionArgs);

            accountList.remove(pos);
            accountList.add(pos, newAcc);
            adapter.notifyDataSetChanged();
        }
    }

    private void loadAccountData(){
        accountList = new ArrayList<String>();
        String[] projection = {TransactionsContract.AccountsEntry.COLUMN_ACCOUNT};
        Cursor c = getContentResolver().query(TransactionsContract.AccountsEntry.CONTENT_URI_ACCOUNTS, projection, null, null, null);

        if(c != null && c.moveToFirst()){
            do{
                String acc = c.getString(c.getColumnIndexOrThrow(TransactionsContract.AccountsEntry.COLUMN_ACCOUNT));
                if(!(accountList.contains(acc))) accountList.add(acc);
            }while (c.moveToNext());
        }
        if(adapter != null) adapter.notifyDataSetChanged();

    }


}
