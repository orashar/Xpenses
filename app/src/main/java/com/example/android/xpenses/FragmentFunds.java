package com.example.android.xpenses;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.xpenses.Adapters.FundListAdapter;
import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.example.android.xpenses.DataTypes.FundObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class FragmentFunds extends Fragment {

    private RecyclerView fundlv;
    private ImageView saveBtn;
    private ImageView addBtn;
    private EditText fundTitleet, fundAccet, fundTypeet, fundBalet, fundDescet;
    private static TextView totalMoneyRemainingtv, totalGreentv, totalRedtv;

    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheet;

    static FundListAdapter adapter;
    static ArrayList<FundObject> fundList;

    private View.OnClickListener addOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
    };

    private View.OnClickListener saveOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(allOk()) {
                insertFund();
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                fundAccet.setText(null);
                fundBalet.setText(null);
                fundTitleet.setText(null);
                fundDescet.setText(null);
                fundTypeet.setText(null);
            }
            else
                Toast.makeText(getContext(), "Missing Information!", Toast.LENGTH_SHORT).show();
        }
    };

    private boolean allOk() {
        if(fundTitleet.getText().toString().isEmpty()) return false;
        if(fundBalet.getText().toString().isEmpty()) return false;
        if(fundAccet.getText().toString().isEmpty()) return false;
        if(fundTypeet.getText().toString().isEmpty()) return false;
        if(fundDescet.getText().toString().isEmpty()) {
            fundDescet.setText("Unknown description");
            return false;
        }
        ArrayList<String> accountList = MainActivity.getSpinnerList();
        if (accountList != null) {
            if (accountList.contains(fundAccet.getText().toString())) return true;
        }
        return false;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fundView = inflater.inflate(R.layout.fragment_funds, container, false);

        totalMoneyRemainingtv = fundView.findViewById(R.id.total_amount_tv);
        totalGreentv = fundView.findViewById(R.id.total_green_tv);
        totalRedtv = fundView.findViewById(R.id.total_red_tv);
        fundlv = fundView.findViewById(R.id.fund_lv);
        addBtn = fundView.findViewById(R.id.add_btn);
        saveBtn = fundView.findViewById(R.id.save_fund_btn);
        fundTitleet = fundView.findViewById(R.id.fund_title_et);
        fundAccet = fundView.findViewById(R.id.fund_account_et);
        fundBalet = fundView.findViewById(R.id.fund_initial_bal_et);
        fundTypeet = fundView.findViewById(R.id.fund_type_et);
        fundDescet = fundView.findViewById(R.id.fund_desc_et);
        bottomSheet = fundView.findViewById(R.id.bottom_sheet_fund_add);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        saveBtn.setOnClickListener(saveOnCLick);

        String text = MainActivity.getCurrentAccount();
        Log.v("-*/*/-*/-*", text);

        fundList = new ArrayList<>();

        updateList(text);

        fundlv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new FundListAdapter(fundList);
        fundlv.setLayoutManager(layoutManager);
        fundlv.setAdapter(adapter);

        addBtn.setOnClickListener(addOnClick);

        Log.v("FragmentFunds", "onCreate is done");

        return fundView;
    }

    private static void populateheaderData(String g, String r) {
        if(totalGreentv != null && totalRedtv != null && totalMoneyRemainingtv != null) {
            totalGreentv.setText(g);
            totalRedtv.setText(r);
            float t = Float.parseFloat(g) - Float.parseFloat(r);
            totalMoneyRemainingtv.setText(Float.toString(t));
        }
    }

    public void insertFund(){
        String fundTitle = fundTitleet.getText().toString();
        String fundAcc = fundAccet.getText().toString();
        String fundBal = fundBalet.getText().toString();
        String fundType = fundTypeet.getText().toString();
        String fundDesc = fundDescet.getText().toString();
        ContentValues values = new ContentValues();
        values.put(TransactionsContract.FundEntry.COLUMN_FUND, fundTitle);
        values.put(TransactionsContract.FundEntry.COLUMN_ACCOUNT, fundAcc);
        values.put(TransactionsContract.FundEntry.COLUMN_FUND_TYPE, fundType);
        values.put(TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE, fundBal);
        values.put(TransactionsContract.FundEntry.COLUMN_FUND_DESC, fundDesc);
        Uri u = MainActivity.getContextOfApplication().getContentResolver().insert(TransactionsContract.FundEntry.CONTENT_URI_FUND, values);

        updateList(MainActivity.getCurrentAccount());

    }

    public static void updateList(String accountSelected){
        float totalGreen = 0, totalRed = 0;
        if(accountSelected != null) {
            String[] projection = {TransactionsContract.FundEntry.COLUMN_FUND, TransactionsContract.FundEntry.COLUMN_ACCOUNT, TransactionsContract.FundEntry.COLUMN_MONEY_SPENT, TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE, TransactionsContract.FundEntry.COLUMN_FUND_BUDGET};
            String selection = TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
            String[] selectionArgs = {accountSelected};
            if(accountSelected.equals("All accounts")) selection = null;
            if(accountSelected.equals("All accounts")) selectionArgs = null;
            Cursor c = MainActivity.getContextOfApplication().getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, selection, selectionArgs, null);

            if (fundList == null) fundList = new ArrayList<>();
            if (adapter == null) adapter = new FundListAdapter(fundList);
            fundList.clear();
            if (c != null && c.moveToFirst()) {

                do {
                    String fund = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_FUND));
                    String exp = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_MONEY_SPENT));
                    String acc = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_ACCOUNT));
                    String bal = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE));
                    String budget = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_FUND_BUDGET));
                    if(exp == null) exp = Integer.toString(0);
                    if(bal == null) bal = Integer.toString(0);
                    if(budget == null) budget = Integer.toString(0);
                    FundObject fundObj = new FundObject(fund, acc, bal, exp, budget);
                    totalGreen += Float.parseFloat(bal);
                    totalRed += Float.parseFloat(exp);
                    if (!(fundList.contains(fundObj))){
                        fundList.add(fundObj);
                    }

                } while (c.moveToNext());
            }
            populateheaderData(Float.toString(totalGreen), Float.toString(totalRed));

            adapter.notifyDataSetChanged();
        }
    }

}
