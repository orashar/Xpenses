package com.example.android.xpenses;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private ArrayList<String> paymentMethodsList = new ArrayList<>();
    private ArrayList<String> accountList = new ArrayList<>();
    private ArrayAdapter<String> adapterf;

    private BottomSheetBehavior bottomSheetBehaviorf, bottomSheetBehaviora;
    private View bottomSheetf, bottomSheeta;
    private ListView listf, lista;
    private TextView categorytv, subCategorytv, fundstv, accountet, datetv, timetv;//, moretv;
    private EditText amounttv, noteet; //, payeeet, taget, sourceet, purposeet;
    private Button saveBtn, cancelBtn;
    private LinearLayout  addll; //, morell, payeell, sourcell, tagll, purposell;
    private CoordinatorLayout addActivitycl;
    /*private boolean moreLess = true;*/
    private SimpleDateFormat df;
    private Date c;

    private String itemId, ive = "", previousMoney = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        init();


        Intent intent = getIntent();
        itemId = intent.getStringExtra("NEW_OR_OLD");
        if("new expense".equals(itemId)){
            getSupportActionBar().setTitle("Add Expense");
            ive = itemId;
            /*sourcell.setVisibility(View.GONE);
            purposell.setVisibility(View.GONE);*/
        } else if("new income".equals(itemId)){
            getSupportActionBar().setTitle("Add Income");
            ive = itemId;
            categorytv.setVisibility(View.GONE);
            /*
            payeell.setVisibility(View.GONE);
            tagll.setVisibility(View.GONE);*/
        } else{
            getSupportActionBar().setTitle("Update");
            setDataToViews(itemId);
        }
        displayList();

        if(!MainActivity.getCurrentAccount().toLowerCase().equals("all accounts")){
            accountet.setText(MainActivity.getCurrentAccount());
        } else{
            accountet.setText(accountList.get(0));
        }

       /* moretv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtmSheets();
                hideKeyboard();
                if(moreLess){
                    moreLess = false;
                    moretv.setText("Less");
                    morell.setVisibility(View.VISIBLE);
                } else{
                    moreLess = true;
                    moretv.setText("More");
                    morell.setVisibility(View.GONE);
                }
            }
        });*/

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
                //bottomSheetBehaviorc.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
                fundstv.setText(paymentMethodsList.get(position));
                bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if(allOk()) {
                    if(ive.equals(itemId))
                        insertDataInStorage();
                    else
                        updateDataInStorage();
                    finish();
                } else{
                    Toast.makeText(AddActivity.this, "You forgot something important to enter!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        timetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), TimePickyActivity.class), 1);
            }
        });

        final Calendar c = Calendar.getInstance();


        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        datetv.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void closeBtmSheets() {
        if(bottomSheetBehaviorf.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if(bottomSheetBehaviora.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private boolean allOk(){
        return !amounttv.getText().toString().equals("");
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
        datetv = findViewById(R.id.date);
        timetv = findViewById(R.id.time);
        noteet = findViewById(R.id.note);
        /*moretv = findViewById(R.id.more);
        taget = findViewById(R.id.tag);
        payeeet = findViewById(R.id.payee);
        morell = findViewById(R.id.more_ll);
        payeell = findViewById(R.id.payee_ll);
        sourcell = findViewById(R.id.source_ll);
        tagll = findViewById(R.id.tag_ll);
        purposell = findViewById(R.id.purpose_ll);
        sourceet = findViewById(R.id.source);
        purposeet = findViewById(R.id.purpose);*/

        bottomSheetBehaviorf.setHideable(true);
        bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehaviora.setHideable(true);
        bottomSheetBehaviora.setState(BottomSheetBehavior.STATE_HIDDEN);


        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("yyyy-MM-dd");

        datetv.setText(df.format(c.getTime()));
        timetv.setText(getTimeToSet(String.valueOf(c.getHours()), String.valueOf(c.getMinutes())));
    }

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
    }

    private void displayList() {
        loadListDataa();

        adapterf = new ArrayAdapter<>(this, R.layout.payment_method_item, R.id.payment_item, paymentMethodsList);
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

        paymentMethodsList.clear();

        if(c != null && c.moveToFirst()) {
            do {
                String fund = c.getString(c.getColumnIndexOrThrow(TransactionsContract.FundEntry.COLUMN_FUND));
                if (!(paymentMethodsList.contains(fund))) paymentMethodsList.add(fund);

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

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private ContentValues prepareDataValues(){
        ContentValues values = new ContentValues();
        settleFetchableValues();

        if("new income".equals(itemId) || ive.equals("I")){
            values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE, "I");
        } else if ("new expense".equals(itemId) || ive.equals("E")){
            values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE, "E");
        }

        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT, amounttv.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT, accountet.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_CATEGORY, categorytv.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_SUB_CATEGORY, subCategorytv.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_DATE, datetv.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_FUND, fundstv.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TIME, timetv.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_NOTE, noteet.getText().toString());
        /*values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_PAYEE, payeeet.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TAG, taget.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_SOURCE, sourceet.getText().toString());
        values.put(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_PURPOSE, purposeet.getText().toString());*/

        return values;
    }

    private void insertDataInStorage(){

        String[] projection = {""};
        if("new income".equals(itemId)){
            projection[0] = TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE;
        } else if ("new expense".equals(itemId)){
            projection[0] = TransactionsContract.FundEntry.COLUMN_MONEY_SPENT;
        }


        getContentResolver().insert(TransactionsContract.TransactionsEntry.CONTENT_URI, prepareDataValues());


        String selection = TransactionsContract.FundEntry.COLUMN_FUND + "=?" + " AND " + TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
        String[] selectionArgs = {fundstv.getText().toString(), accountet.getText().toString()};
        updateFundsTableWithNewAmount(projection, selection, selectionArgs);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    private void updateDataInStorage(){
        String[] projection = {""};
        if(ive.equals("I")){
            projection[0] = TransactionsContract.FundEntry.COLUMN_INITIAL_BALANCE;
        } else if (ive.equals("E")){
            projection[0] = TransactionsContract.FundEntry.COLUMN_MONEY_SPENT;
        }
        String selectionT = TransactionsContract.TransactionsEntry._ID + "=?";
        String[] selectionArgsT = {itemId};
        getContentResolver().update(TransactionsContract.TransactionsEntry.CONTENT_URI, prepareDataValues(), selectionT, selectionArgsT);

        String selection = TransactionsContract.FundEntry.COLUMN_FUND + "=?" + " AND " + TransactionsContract.FundEntry.COLUMN_ACCOUNT + "=?";
        String[] selectionArgs = {fundstv.getText().toString(), accountet.getText().toString()};
        updateFundsTableWithOldAmount(projection, selection, selectionArgs, previousMoney);
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
    }

    private void updateFundsTableWithOldAmount(String[] projection, String selection, String[] selectionArgs, String previousMoney){
        Cursor c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, selection, selectionArgs, null);
        if(c != null && c.moveToFirst()) {
            String currentAmount = c.getString(c.getColumnIndexOrThrow(projection[0]));
            String finalAmount;
            if(currentAmount == null) currentAmount = Integer.toString(0);
            finalAmount = Integer.toString(Integer.parseInt(currentAmount) + Integer.parseInt(amounttv.getText().toString()) - Integer.parseInt(previousMoney));
            Log.v("VALgjdkj", Integer.parseInt(currentAmount) + " + " + Integer.parseInt(amounttv.getText().toString()) + "-" + Integer.parseInt(previousMoney));

            ContentValues valuesF = new ContentValues();
            valuesF.put(projection[0], finalAmount);
            getContentResolver().update(TransactionsContract.FundEntry.CONTENT_URI_FUND, valuesF, selection, selectionArgs);
        }
    }

    private void updateFundsTableWithNewAmount(String[] projection, String selection, String[] selectionArgs){
        Cursor c = getContentResolver().query(TransactionsContract.FundEntry.CONTENT_URI_FUND, projection, selection, selectionArgs, null);
        if(c != null && c.moveToFirst()) {
            String previousAmount = c.getString(c.getColumnIndexOrThrow(projection[0]));
            String finalAmount;
            if(previousAmount == null) previousAmount = Integer.toString(0);
                finalAmount = Integer.toString(Integer.parseInt(previousAmount) + Integer.parseInt(amounttv.getText().toString()));

            ContentValues valuesF = new ContentValues();
            valuesF.put(projection[0], finalAmount);
            getContentResolver().update(TransactionsContract.FundEntry.CONTENT_URI_FUND, valuesF, selection, selectionArgs);
        }
    }

    private void setDataToViews(String itemId){
        String[] projection = null;
        String selection = TransactionsContract.TransactionsEntry._ID + "=?";
        String[] selectionArgs = {itemId};
        Cursor c = getContentResolver().query(TransactionsContract.TransactionsEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        if (c != null) {
            c.moveToFirst();
            String amount = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT));
            String category = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_CATEGORY));
            String subCategory = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_SUB_CATEGORY));
            String date = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_DATE));
            String account = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT));
            String type = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE));
            String fund = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_FUND));
            String time = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TIME));
            String note = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_NOTE));
            /*String tag = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TAG));
            String source = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_SOURCE));
            String purpose = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_PURPOSE));
            String payee = c.getString(c.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_PAYEE));*/
            previousMoney = amount;
            amounttv.setText(amount);
            accountet.setText(account);
            Log.v("*-/-*-*", type );
            if("I".equals(type)){
                ive = "I";
                /*sourceet.setText(source);
                purposeet.setText(purpose);*/
                categorytv.setVisibility(View.GONE);
                amounttv.setTextColor(getResources().getColor(R.color.textIncome));
            } else{
                ive = "E";
                /*payeeet.setText(payee);
                taget.setText(tag);*/
                categorytv.setText(category);
                subCategorytv.setVisibility(View.VISIBLE);
                subCategorytv.setText(subCategory);
                amounttv.setTextColor(getResources().getColor(R.color.textExpense));
            }
            fundstv.setText(fund);
            datetv.setText(date);
            timetv.setText(time);
            noteet.setText(note);
        }
    }

    private void settleFetchableValues(){
        if(categorytv.getText().equals("category")) categorytv.setText("unknown 5");
        if(fundstv.getText().equals("fund")) fundstv.setText("unknown fund");
        if(noteet.getText().equals("")) noteet.setText("unknown note");
        /*if(sourceet.getText().equals("")) sourceet.setText("unknown source");
        if(purposeet.getText().equals("")) purposeet.setText("unknown purpose");
        if(payeeet.getText().equals("")) payeeet.setText("unknown payee");
        if(taget.getText().equals("")) taget.setText("unknown tag");*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String timeToSet = data.getStringExtra("TIME_TO_SET");
                timetv.setText(timeToSet);
            }
        } else if(requestCode == 2){
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


    /*@Override public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if ((bottomSheetBehaviorc.getState()==BottomSheetBehavior.STATE_COLLAPSED) || (bottomSheetBehaviorf.getState()==BottomSheetBehavior.STATE_EXPANDED)) {

                Rect outRect = new Rect();
                bottomSheetc.getGlobalVisibleRect(outRect);
                bottomSheetf.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    bottomSheetBehaviorc.setState(BottomSheetBehavior.STATE_HIDDEN);
                    bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else{
                    if(bottomSheetBehaviorc.getState()==BottomSheetBehavior.STATE_COLLAPSED){
                        listc.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                subCategorytv.setText(categoryList.get(groupPosition).getSubCategoryList().get(childPosition).getName());
                                subCategorytv.setVisibility(View.VISIBLE);
                                categorytv.setText(categoryList.get(groupPosition).getName());
                                bottomSheetBehaviorc.setState(BottomSheetBehavior.STATE_HIDDEN);
                                return false;
                            }
                        });
                    } else if(bottomSheetBehaviorf.getState()==BottomSheetBehavior.STATE_EXPANDED){
                        listf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.v("listClick*-*-*-", "*-*--*-**-*-*--*-*");
                                fundstv.setText(paymentMethodsList.get(position));
                                bottomSheetBehaviorf.setState(BottomSheetBehavior.STATE_HIDDEN);
                            }
                        });
                    }
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }
*/
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
