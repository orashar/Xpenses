package com.example.android.xpenses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.example.android.xpenses.DataFiles.TransactionsContract.BudgetEntry;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BudgetActivity extends AppCompatActivity {


    private ArrayList<String> fundsList = new ArrayList<>();
    private ArrayList<String> accountList = new ArrayList<>();
    private ArrayAdapter<String> adapterf;

    private BottomSheetBehavior bottomSheetBehaviorf, bottomSheetBehaviora;
    private View bottomSheetf, bottomSheeta;
    private ListView listf, lista;
    private TextView categorytv, subCategorytv, fundstv, accountet, startDatetv, endDatetv;
    private EditText amounttv, noteet;
    private Button saveBtn, cancelBtn;
    private LinearLayout addll;
    private CoordinatorLayout addActivitycl;
    private SimpleDateFormat df;
    private Date c;

    private String whoseDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        init();

        getSupportActionBar().setTitle("Set Budget");
        displayList();


        addll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
            }
        });

        fundstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
                loadListDataf(accountet.getText().toString());
                if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        accountet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
                if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        categorytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
                startActivityForResult(new Intent(getApplicationContext(), CategoryActivity.class), 2);
            }
        });

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

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountet.setText(accountList.get(position));
                bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        listf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("listClick*-*-*-", "*-*--*-**-*-*--*-*");
                fundstv.setText(fundsList.get(position));
                bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
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
                    Toast.makeText(BudgetActivity.this, "You forgot something important to enter!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Calendar c = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String dateString = year + "-" + monthOfYear + "-" + dayOfMonth;
                        if(whoseDate.equals("start")){
                            startDatetv.setText(dateString);
                        } else if(whoseDate.equals("end")){
                            endDatetv.setText(dateString);
                        }

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        startDatetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whoseDate = "start";
                datePickerDialog.show();
            }
        });

        endDatetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whoseDate = "end";
                datePickerDialog.show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init(){
        bottomSheetf = findViewById(R.id.bottom_sheet_f);
        bottomSheeta = findViewById(R.id.bottom_sheet_a);
        bottomSheetBehaviorf = BottomSheetBehavior.from(bottomSheetf);
        bottomSheetBehaviora = BottomSheetBehavior.from(bottomSheeta);
        addActivitycl = findViewById(R.id.activity_cl);
        subCategorytv = findViewById(R.id.sub_category_tv);
        categorytv = findViewById(R.id.category_tv);
        listf = findViewById(R.id.list_f);
        lista = findViewById(R.id.list_a);
        saveBtn = findViewById(R.id.save_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        amounttv = findViewById(R.id.amount);
        accountet = findViewById(R.id.account);
        fundstv = findViewById(R.id.funds);
        addll = findViewById(R.id.add_ll);
        startDatetv = findViewById(R.id.start_date);
        noteet = findViewById(R.id.note);
        endDatetv = findViewById(R.id.end_date);

        bottomSheetBehaviorf.setHideable(true);
        bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehaviora.setHideable(true);
        bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);


        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("yyyy-MM-dd");

        startDatetv.setText(df.format(c.getTime()));
        endDatetv.setText(df.format(c.getTime()));
        /*timetv.setText(getTimeToSet(String.valueOf(c.getHours()), String.valueOf(c.getMinutes())));*/
    }

/*
    private String getTimeToSet(String hour, String min){
        String ampm = "am";
        min = getIntToSet(min);
        if(Integer.parseInt(hour) == 0){
            hour = Integer.toString(12);
        }
        else if(Integer.parseInt(hour) >= 12){
            hour = Integer.toString(Integer.parseInt(hour) - 12);
            if(Integer.parseInt(hour) == 0) hour = Integer.toString(12);
            ampm = "pm";
        }

        hour = getIntToSet(hour);

        String timeString = hour+" : "+min+" "+ampm;

        return timeString;

    }

    private String getIntToSet(String intToSet) {
        String zero = "0";
        ArrayList<Integer> singleDigit = new ArrayList<>();
        singleDigit.add(0);
        singleDigit.add(1);
        singleDigit.add(2);
        singleDigit.add(3);
        singleDigit.add(4);
        singleDigit.add(5);
        singleDigit.add(6);
        singleDigit.add(7);
        singleDigit.add(8);
        singleDigit.add(9);
        if(singleDigit.contains(Integer.parseInt(intToSet))){
            intToSet = zero + intToSet;
        }
        return intToSet;
    }*/

    private void displayList() {
        loadListDataa();

        adapterf = new ArrayAdapter<>(this, R.layout.payment_method_item, R.id.payment_item, fundsList);
        listf.setAdapter(adapterf);
        ArrayAdapter<String> adaptera = new ArrayAdapter<>(this, R.layout.payment_method_item, R.id.payment_item, accountList);
        lista.setAdapter(adaptera);
    }




    private void loadListDataf(String accountName){
        Log.v("-*-*-Loading funds", "-*-*/-/-/-/" + accountName);
        String[] projection = {TransactionsContract.FundEntry.COLUMN_FUND};
        String selection = TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
        String[] selectionArgs = {accountName};
        Cursor c;

        if(!(accountName.equals("account"))){
            c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, selection, selectionArgs, null);
        } else{
            c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, null, null, null);
        }

        fundsList.clear();

        if(c != null && c.moveToFirst()) {
            do {
                String fund = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_FUND));
                if (!(fundsList.contains(fund))) fundsList.add(fund);

            } while (c.moveToNext());
        }
        if(adapterf != null) adapterf.notifyDataSetChanged();

    }
    private void loadListDataa(){
        String[] projection = {TransactionsContract.AccountsEntry.COLUMN_ACCOUNT};

        Cursor c = getContentResolver().query(TransactionsContract.AccountsEntry.CONTENT_URI_ACCOUNTS, projection, null, null, null);

        if(c != null && c.moveToFirst()) {
            do {
                String acc = c.getString(c.getColumnIndexOrThrow(TransactionsContract.AccountsEntry.COLUMN_ACCOUNT));
                if (!(accountList.contains(acc))) accountList.add(acc);

            } while (c.moveToNext());
        }
    }


    private ContentValues prepareDataValues(){
        ContentValues values = new ContentValues();
        settleFetchableValues();

        values.put(BudgetEntry.COLUMN_BUDGET_AMOUNT, amounttv.getText().toString());
        values.put(BudgetEntry.COLUMN_BUDGET_ACCOUNT, accountet.getText().toString());
        values.put(BudgetEntry.COLUMN_BUDGET_FUND, fundstv.getText().toString());
        values.put(BudgetEntry.COLUMN_BUDGET_CATEGORY, categorytv.getText().toString());
        values.put(BudgetEntry.COLUMN_BUDGET_SUB_CATEGORY, subCategorytv.getText().toString());
        values.put(BudgetEntry.COLUMN_BUDGET_START_DATE, startDatetv.getText().toString());
        values.put(BudgetEntry.COLUMN_BUDGET_END_DATE, endDatetv.getText().toString());
        values.put(BudgetEntry.COLUMN_BUDGET_NOTE, noteet.getText().toString());
        return values;
    }

    private void settleFetchableValues(){
        if(categorytv.getText().equals("category")) {
            categorytv.setText("unknown category");
            subCategorytv.setText("unknown sub category");
        }
        if(noteet.getText().equals("")) noteet.setText("unknown note");
    }


    private void insertDataInStorage(){

        ContentValues budgetValues = prepareDataValues();
        Uri i  = getContentResolver().insert(BudgetEntry.CONTENT_URI_BUDGET, budgetValues);
        if(i == null){
            Log.v("NullUriBudget", "null uri received in budget");
        } else{
            Log.v("UriBudget", "uri received in budget");

        }

        String[] projectionF = {TransactionsContract.FundEntry.COLUMN_FUND_BUDGET};
        String selectionF = TransactionsContract.FundEntry.COLUMN_FUND + "=?" + " AND " + TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
        String[] selectionArgsF = {fundstv.getText().toString(), accountet.getText().toString()};

        String[] projectionC = {TransactionsContract.CategoryEntry.COLUMN_CATEGORY_BUDGET};
        String selectionC = TransactionsContract.CategoryEntry.COLUMN_CATEGORY + "=?" + " AND " + TransactionsContract.CategoryEntry.COLUMN_SUB_CATEGORY + "=?";
        String[] selectionArgsC = {categorytv.getText().toString(), subCategorytv.getText().toString()};

        updateFundsTable(projectionF, selectionF, selectionArgsF);
        updateCategoryTable(projectionC, selectionC, selectionArgsC);
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    private void updateFundsTable(String[] projection, String selection, String[] selectionArgs){
        Cursor c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, selection, selectionArgs, null);
        if(c != null && c.moveToFirst()) {
            String currentAmount = c.getString(c.getColumnIndexOrThrow(projection[0]));
            String finalAmount;
            if(currentAmount == null) currentAmount = Integer.toString(0);
            finalAmount = Integer.toString(Integer.parseInt(currentAmount) + Integer.parseInt(amounttv.getText().toString()));
            Log.v("VALgjdkj", Integer.parseInt(currentAmount) + " + " + Integer.parseInt(amounttv.getText().toString()));

            ContentValues valuesF = new ContentValues();
            valuesF.put(projection[0], finalAmount);
            getContentResolver().update(TransactionsContract.FundEntry.CONTENT_URI_FUND, valuesF, selection, selectionArgs);
        }
    }

    private void updateCategoryTable(String[] projectionC, String selectionC, String[] selectionArgsC){
        Cursor c = getContentResolver().query(TransactionsContract.CategoryEntry.CONTENT_URI_CATEGORY, projectionC, selectionC, selectionArgsC, null);
        if(c != null && c.moveToFirst()) {
            String currentAmount = c.getString(c.getColumnIndexOrThrow(projectionC[0]));
            String finalAmount;
            if(currentAmount == null) currentAmount = Integer.toString(0);
            finalAmount = Integer.toString(Integer.parseInt(currentAmount) + Integer.parseInt(amounttv.getText().toString()));
            Log.v("VALgjdkj", Integer.parseInt(currentAmount) + " + " + Integer.parseInt(amounttv.getText().toString()));

            ContentValues valuesC = new ContentValues();
            valuesC.put(projectionC[0], finalAmount);
            getContentResolver().update(TransactionsContract.CategoryEntry.CONTENT_URI_CATEGORY, valuesC, selectionC, selectionArgsC);
        }
    }


    private void closeBtmSheets() {
        if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private boolean allOk(){
        boolean ret = true;

        if(fundstv.getText().equals("fund")) ret = false;
        if(amounttv.getText().toString().equals("")) ret = false;
        return ret;
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String timeToSet = data.getStringExtra("TIME_TO_SET");
                timetv.setText(timeToSet);
            }
        } else */if(requestCode == 2){
            if(resultCode == RESULT_OK){
                String[] cscArr = data.getStringArrayExtra("CATEGORY_AND_SUBCATEGORY");
                categorytv.setText(cscArr[0]);
                subCategorytv.setText(cscArr[1]);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if((bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_COLLAPSED) || (bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_EXPANDED)){
            bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else{
            super.onBackPressed();
        }
    }

}
