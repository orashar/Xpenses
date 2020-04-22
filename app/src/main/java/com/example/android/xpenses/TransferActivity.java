package com.example.android.xpenses;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.xpenses.Adapters.BottomListAdapter;
import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.example.android.xpenses.DataTypes.TitleAmountObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.NumberFormat;
import java.util.ArrayList;

public class TransferActivity extends AppCompatActivity {

    private TextView accountFromtv, accountTotv, fundTotv, fundFromtv;
    private EditText amountet, descet;
    private ListView lista, listf;
    private Button cancelBtn, saveBtn;
    private BottomSheetBehavior bottomSheetBehaviora, bottomSheetBehaviorf;
    private View bottomSheeta, bottomSheetf;
    private ArrayList<TitleAmountObject> fundList, accountList;
    private BottomListAdapter adaptera, adapterf;

    private String accountClicked = "", fundClicked = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        init();


        bottomSheetBehaviorf.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    hideKeyboard();
                    bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        bottomSheetBehaviora.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    hideKeyboard();
                    bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    private void init(){

        fundList = new ArrayList<>();
        accountList = new ArrayList<>();

        lista = findViewById(R.id.list_a);
        listf = findViewById(R.id.list_f);

        amountet = findViewById(R.id.amount);
        accountFromtv = findViewById(R.id.account_from);
        accountTotv = findViewById(R.id.account_to);
        fundFromtv = findViewById(R.id.fund_from);
        fundTotv = findViewById(R.id.fund_to);
        descet = findViewById(R.id.note);
        cancelBtn = findViewById(R.id.cancel_btn);
        saveBtn = findViewById(R.id.save_btn);
        bottomSheeta = findViewById(R.id.bottom_sheet_a);
        bottomSheetf = findViewById(R.id.bottom_sheet_f);
        bottomSheetBehaviora = BottomSheetBehavior.from(bottomSheeta);
        bottomSheetBehaviorf = BottomSheetBehavior.from(bottomSheetf);

        adapterf = new BottomListAdapter(this, fundList);
        listf.setAdapter(adapterf);
        adaptera = new BottomListAdapter(this, accountList);
        lista.setAdapter(adaptera);

        bottomSheetBehaviorf.setHideable(true);
        bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehaviora.setHideable(true);
        bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);

        accountFromtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
                loadListDataa();
                accountClicked = "from";
                Toast.makeText(TransferActivity.this, "opening", Toast.LENGTH_SHORT).show();
                if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        accountTotv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
                loadListDataa();
                accountClicked = "to";
                if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        fundTotv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
                loadListDataf(accountTotv.getText().toString());
                fundClicked = "to";
                if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        fundFromtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
                loadListDataf(accountFromtv.getText().toString());
                fundClicked = "from";
                if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(accountClicked.equals("from")) accountFromtv.setText(accountList.get(position).getTitle());
                else accountTotv.setText(accountList.get(position).getTitle());
                bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        listf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(fundClicked.equals("from")) fundFromtv.setText(fundList.get(position).getTitle());
                else fundTotv.setText(fundList.get(position).getTitle());
                bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if(allOk()) {
                    insertDataInStorage();
                    finish();
                } else{
                    Toast.makeText(TransferActivity.this, "You forgot something important to enter!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertDataInStorage() {
        getContentResolver().insert(TransactionsContract.TransferEntry.CONTENT_URI_TRANSFERS, prepareDataValues());

        updateFromFund();
        updateToFund();


    }

    private void updateToFund() {
        String[] projection = {TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE};
        String selection = TransactionsContract.FundEntry.COLUMN_FUND + "=? AND " + TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
        String[] selectionArgs = {fundTotv.getText().toString(), accountTotv.getText().toString()};

        Cursor c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, selection, selectionArgs, null);
        if(c != null){
            c.moveToFirst();
            String bal = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE));
            bal = Integer.toString(Integer.parseInt(bal) + Integer.parseInt(amountet.getText().toString()));

            ContentValues v = new ContentValues();
            v.put(TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE, bal);
            getContentResolver().update(TransactionsContract.FundEntry.CONTENT_URI_FUND, v, selection, selectionArgs);
        }

    }

    private void updateFromFund() {
        String[] projection = {TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE};
        String selection = TransactionsContract.FundEntry.COLUMN_FUND + "=? AND " + TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
        String[] selectionArgs = {fundFromtv.getText().toString(), accountFromtv.getText().toString()};

        Cursor c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, selection, selectionArgs, null);
        if(c != null){
            c.moveToFirst();
            String bal = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE));
            bal = Integer.toString(Integer.parseInt(bal) - Integer.parseInt(amountet.getText().toString()));

            ContentValues v = new ContentValues();
            v.put(TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE, bal);
            getContentResolver().update(TransactionsContract.FundEntry.CONTENT_URI_FUND, v, selection, selectionArgs);
        }
    }

    private ContentValues prepareDataValues(){
        ContentValues values = new ContentValues();

        if(descet.getText().toString().isEmpty()) descet.setText("Unknown description.");

        values.put(TransactionsContract.TransferEntry.COLUMN_TRANSFER_AMMOUNT, amountet.getText().toString());
        values.put(TransactionsContract.TransferEntry.COLUMN_ACCOUNT_FROM, accountFromtv.getText().toString());
        values.put(TransactionsContract.TransferEntry.COLUMN_ACCOUNT_TO, accountTotv.getText().toString());
        values.put(TransactionsContract.TransferEntry.COLUMN_FUND_FROM, fundFromtv.getText().toString());
        values.put(TransactionsContract.TransferEntry.COLUMN_FUND_TO, fundTotv.getText().toString());
        values.put(TransactionsContract.TransferEntry.COLUMN_TRANSFER_DESC, descet.getText().toString());

        return values;
    }

    private boolean allOk(){
        return !amountet.getText().toString().equals("");
    }

    private void loadListDataf(String accountName){
        Log.v("-*-*-Loading funds", "-*-*/-/-/-/" + accountName);
        String[] projection = {TransactionsContract.FundEntry.COLUMN_FUND, TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE, TransactionsContract.FundEntry.COLUMN_MONEY_SPENT};
        String selection = TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
        String[] selectionArgs = {accountName};
        Cursor c;

        if(!(accountName.equals("account"))){
            c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, selection, selectionArgs, null);
        } else{
            c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, null, null, null);
        }

        fundList.clear();

        if(c != null && c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_FUND));
                String green = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE));
                String red = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_MONEY_SPENT));
                if(green == null) green = "0";
                if(red == null) red = "0";
                String t = NumberFormat.getCurrencyInstance().format(Float.parseFloat(green) - Float.parseFloat(red));

                TitleAmountObject fund = new TitleAmountObject(title, t);
                if (!(fundList.contains(fund))) fundList.add(fund);

            } while (c.moveToNext());
        }
        if(adapterf != null) adapterf.notifyDataSetChanged();

    }
    private void loadListDataa(){
        String[] projection = {TransactionsContract.AccountsEntry.COLUMN_ACCOUNT};
        accountList.clear();


        Cursor c = getContentResolver().query(TransactionsContract.AccountsEntry.CONTENT_URI_ACCOUNTS, projection, null, null, null);

        if(c != null && c.moveToFirst()) {
            do {
                String acc = c.getString(c.getColumnIndexOrThrow(TransactionsContract.AccountsEntry.COLUMN_ACCOUNT));
                String[] projF = {TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE, TransactionsContract.FundEntry.COLUMN_MONEY_SPENT};
                String selection = TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
                String[] selectoinArgs = {acc};
                Cursor cdash = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projF, selection, selectoinArgs, null);
                float amount = 0;
                if(cdash != null){
                    while (cdash.moveToNext()){

                        String g = cdash.getString(cdash.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE));
                        String r = cdash.getString(cdash.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_MONEY_SPENT));
                        if(g == null) g = "0";
                        if(r == null) r = "0";
                        amount = (amount + Float.parseFloat(g) - Float.parseFloat(r));
                    }
                }
                TitleAmountObject account = new TitleAmountObject(acc, NumberFormat.getCurrencyInstance().format(amount));
                if (!(accountList.contains(account))) accountList.add(account);

            } while (c.moveToNext());
        }
        adaptera.notifyDataSetChanged();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void closeBtmSheets() {
        if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }
    @Override
    public void onBackPressed() {
        if((bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_COLLAPSED) || (bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_EXPANDED)){
            closeBtmSheets();
        } else{
            super.onBackPressed();
        }
    }

}
