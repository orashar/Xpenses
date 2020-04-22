package com.example.android.xpenses;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.android.xpenses.Adapters.TransactionsCursorAdapter;
import com.example.android.xpenses.DataFiles.TransactionsContract;

import java.util.ArrayList;

import static com.example.android.xpenses.DataFiles.TransactionsContract.TransactionsEntry._ID;

public class AllTransactionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TRANSACTION_LOADER = 1;
    private TransactionsCursorAdapter transactionsCursorAdapter;
    private Cursor curs;

    public Spinner accountSpinner;
    ArrayAdapter<String> accountSpinnerAdapter;
    static ArrayList<String> accountList;

    private ListView lv;

    private AdapterView.OnItemClickListener lvOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            curs.moveToPosition(position);
            String itemId = curs.getString(curs.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry._ID));
            Intent intent = new Intent(AllTransactionActivity.this, AddActivity.class);
            intent.putExtra("NEW_OR_OLD", itemId);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_transactions);

        lv = findViewById(R.id.all_transactions_lv);
        accountSpinner = findViewById(R.id.account_spinner);
        accountSpinner = findViewById(R.id.account_spinner);

        accountList = new ArrayList<>();
        loadAccountData();

        accountSpinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, accountList);
        accountSpinner.setAdapter(accountSpinnerAdapter);

        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bundle loaderArgs = new Bundle();
                loaderArgs.putString("selectedAccount", accountList.get(position));
                getSupportLoaderManager().restartLoader(TRANSACTION_LOADER, loaderArgs, AllTransactionActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        transactionsCursorAdapter = new TransactionsCursorAdapter(this, null, "all");
        lv.setAdapter(transactionsCursorAdapter);

        lv.setOnItemClickListener(lvOnClick);

        String accountSelected = MainActivity.getCurrentAccount();
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString("selectedAccount", accountSelected);

        getSupportLoaderManager().initLoader(TRANSACTION_LOADER, loaderArgs, this);

    }

    private void loadAccountData(){
        accountList = new ArrayList<>();
        String[] projection = {TransactionsContract.AccountsEntry.COLUMN_ACCOUNT};
        Cursor c = getContentResolver().query(TransactionsContract.AccountsEntry.CONTENT_URI_ACCOUNTS, projection, null, null, null);

        if(c != null && c.moveToFirst()){
            do{
                String acc = c.getString(c.getColumnIndexOrThrow(TransactionsContract.AccountsEntry.COLUMN_ACCOUNT));
                if(!(accountList.contains(acc))) accountList.add(acc);
            }while (c.moveToNext());
        }
        if(accountSpinnerAdapter != null) accountSpinnerAdapter.notifyDataSetChanged();

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {TransactionsContract.TransactionsEntry._ID,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_CATEGORY,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_DATE,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_FUND,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TAG,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_PURPOSE,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_SOURCE};
        String sortOrder = _ID+" DESC";
        String selection = TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT + "=?";
        String[] selectionArgs = {""};
        if(args.getString("selectedAccount").equals("All accounts")) {
            selection = null;
            selectionArgs = null;
        }
        else {
            selectionArgs[0] = args.getString("selectedAccount");
        }
        return new CursorLoader(this, TransactionsContract.TransactionsEntry.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        curs = cursor;
        transactionsCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        curs = null;
        transactionsCursorAdapter.swapCursor(null);
    }
}
