package com.example.android.xpenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.xpenses.Adapters.BudgetHistoryAdapter;
import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.example.android.xpenses.DataFiles.TransactionsContract.BudgetEntry;
import com.example.android.xpenses.DataTypes.BudgetObject;

import java.util.ArrayList;

import static android.view.View.GONE;

public class BudgetHistroyActivity extends AppCompatActivity {

    private RecyclerView budgetHistoryrv;
    private BudgetHistoryAdapter budgetHistoryAdapter;

    private ArrayList<BudgetObject> budgetHistoryList;

    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_histroy);

        budgetHistoryrv = findViewById(R.id.budget_history_rv);
        emptyView = findViewById(R.id.budget_list_empty_view);

        findViewById(R.id.add_budget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BudgetHistroyActivity.this, BudgetActivity.class));
            }
        });

        budgetHistoryList = new ArrayList<>();
        loadBudgetList();

        if(budgetHistoryList.size() < 1){
            budgetHistoryrv.setVisibility(GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        budgetHistoryAdapter = new BudgetHistoryAdapter(budgetHistoryList);
        budgetHistoryrv.setAdapter(budgetHistoryAdapter);


        budgetHistoryrv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadBudgetList() {

        String[] projection = {BudgetEntry.BID, BudgetEntry.COLUMN_BUDGET_AMOUNT, BudgetEntry.COLUMN_BUDGET_FUND, BudgetEntry.COLUMN_BUDGET_START_DATE, BudgetEntry.COLUMN_BUDGET_END_DATE};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = BudgetEntry.BID + " DESC";

        Cursor budgetCursor = getContentResolver().query(TransactionsContract.BudgetEntry.CONTENT_URI_BUDGET, projection, selection, selectionArgs, sortOrder);
        if(budgetCursor != null){
            while (budgetCursor.moveToNext()){
                String amount = budgetCursor.getString(budgetCursor.getColumnIndexOrThrow(BudgetEntry.COLUMN_BUDGET_AMOUNT));
                String fund = budgetCursor.getString(budgetCursor.getColumnIndexOrThrow(BudgetEntry.COLUMN_BUDGET_FUND));
                String startDate = budgetCursor.getString(budgetCursor.getColumnIndexOrThrow(BudgetEntry.COLUMN_BUDGET_START_DATE));
                String endDate = budgetCursor.getString(budgetCursor.getColumnIndexOrThrow(BudgetEntry.COLUMN_BUDGET_END_DATE));

                BudgetObject budgetObject = new BudgetObject(amount, null, fund, null, null, startDate, endDate);
                budgetHistoryList.add(budgetObject);
            }
        }
    }
}
