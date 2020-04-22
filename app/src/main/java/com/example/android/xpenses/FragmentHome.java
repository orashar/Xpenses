package com.example.android.xpenses;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.android.xpenses.Adapters.TransactionListAdapter;
import com.example.android.xpenses.Adapters.ViewPagerAdpater;
import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.example.android.xpenses.DataTypes.TransactionObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;
import static com.example.android.xpenses.DataFiles.TransactionsContract.TransactionsEntry._ID;

public class FragmentHome extends Fragment {

    private boolean isfabOpen = false;
    private static TransactionListAdapter transactionListAdapter;
    static Bundle loaderArgs = new Bundle();
    static View homeView;

    static ArrayList<TransactionObject> transactionList;

    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton addFab, expenseFab, incomeFab, transferFab, budgetFab;
    private RecyclerView transactionslv;
    private CardView daycv, weekcv, monthcv, yearcv;
    private LinearLayout allTransactionstv, cardOptionsll;
    private TextView expensetv, incometv, transfertv, budgettv;
    private static View emptyTransactions;
    TextView todayRedtv, weekRedtv, monthRedtv, yearRedtv, todayGreentv, weekGreentv, monthGreentv, yearGreentv, dateSpantv, titleCardtv, amountCardtv, optionTodaytv, optionWeektv, optionMonthtv, optionYeartv;
    ImageView moreOptionsiv;

    private View fabLayer;

    private ViewPager viewPager;

    Integer[] imgId = {R.drawable.add, R.drawable.home, R.drawable.funds, R.drawable.stats};
    String[] imgName = {"add", "home", "funds", "stats"};
    Timer timer;

    private Animation fabOpen, fabClose;

    List<String> currentWeekDays = new ArrayList<>();
    List<String> currentWeekMonth = new ArrayList<>();

    private View.OnClickListener fabOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isfabOpen){
                hideFabs();
            } else{
                fabLayer.setVisibility(View.VISIBLE);
                showFabs();
            }
        }
    };

    private View.OnClickListener expenseFabOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Adding Expense", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), AddActivity.class);
            intent.putExtra("NEW_OR_OLD", "new expense");
            startActivity(intent);
        }
    };

    private View.OnClickListener incomeFabOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getContext(), "Adding Income", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), AddActivity.class);
            intent.putExtra("NEW_OR_OLD", "new income");
            startActivity(intent);
        }
    };

    private View.OnClickListener transferFabOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Initialising Transfer", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), TransferActivity.class));
        }
    };

    private View.OnClickListener budgetFabOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Initialising Budget", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), BudgetHistroyActivity.class));
        }
    };

    private AdapterView.OnItemClickListener transationOnCick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Toast.makeText(getContext(), "Transation details", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener daycvOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getContext(), "today", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener weekcvOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getContext(), "This week", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener monthcvOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getContext(), "This month", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener yearcvOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getContext(), "This year", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener allTransactionsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getContext(), "All Transactions", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), AllTransactionActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener emptyOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener fabLayerOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isfabOpen){
                hideFabs();
            }
            hideCardOptions();
        }
    };

    private View.OnClickListener cardMoreOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fabLayer.setVisibility(View.VISIBLE);
            showCardOptions();
        }
    };

    private void showCardOptions() {
        cardOptionsll.setVisibility(View.VISIBLE);
    }

    private void hideCardOptions(){
        if(cardOptionsll.getVisibility() == View.VISIBLE){
            cardOptionsll.setVisibility(GONE);
            fabLayer.setVisibility(GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        transactionList = new ArrayList<>();

        transactionslv = homeView.findViewById(R.id.transaction_list);
        addFab = homeView.findViewById(R.id.add_fab);
        expenseFab = homeView.findViewById(R.id.expense_fab);
        incomeFab = homeView.findViewById(R.id.income_fab);
        transferFab = homeView.findViewById(R.id.transafer_fab);
        budgetFab = homeView.findViewById(R.id.budget_fab);
        daycv = homeView.findViewById(R.id.day_cardview);
        weekcv = homeView.findViewById(R.id.week_cardview);
        monthcv = homeView.findViewById(R.id.month_cardview);
        yearcv = homeView.findViewById(R.id.year_cardview);
        allTransactionstv = homeView.findViewById(R.id.all_transactions);
        expensetv = homeView.findViewById(R.id.add_expense_tv);
        incometv = homeView.findViewById(R.id.add_income_tv);
        transfertv = homeView.findViewById(R.id.add_transfer_tv);
        budgettv = homeView.findViewById(R.id.add_budget_tv);
        viewPager = homeView.findViewById(R.id.view_pager);
        emptyTransactions = homeView.findViewById(R.id.empty_transaction);
        amountCardtv = homeView.findViewById(R.id.balance_tv);
        titleCardtv = homeView.findViewById(R.id.today_title_tv);
        dateSpantv = homeView.findViewById(R.id.date_span_tv);
        moreOptionsiv = homeView.findViewById(R.id.more_option_iv);

        todayRedtv = homeView.findViewById(R.id.today_red_tv);
        weekRedtv = homeView.findViewById(R.id.week_red_tv);
        monthRedtv = homeView.findViewById(R.id.month_red_tv);
        yearRedtv = homeView.findViewById(R.id.year_red_tv);
        todayGreentv = homeView.findViewById(R.id.today_green_tv);
        weekGreentv = homeView.findViewById(R.id.week_green_tv);
        monthGreentv = homeView.findViewById(R.id.month_green_tv);
        yearGreentv = homeView.findViewById(R.id.year_green_tv);

        fabLayer = homeView.findViewById(R.id.fab_layer);
        fabLayer.setOnClickListener(fabLayerOnCLick);


        PagerAdapter pagerAdapter = new ViewPagerAdpater(getActivity(), imgId, imgName);
        viewPager.setAdapter(pagerAdapter);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % imgId.length);
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 3000, 5000);

        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        addFab.setOnClickListener(fabOnCLick);
        expenseFab.setOnClickListener(expenseFabOnCLick);
        expensetv.setOnClickListener(expenseFabOnCLick);
        incomeFab.setOnClickListener(incomeFabOnCLick);
        incometv.setOnClickListener(incomeFabOnCLick);
        transferFab.setOnClickListener(transferFabOnCLick);
        transfertv.setOnClickListener(transferFabOnCLick);
        budgetFab.setOnClickListener(budgetFabOnCLick);
        budgettv.setOnClickListener(budgetFabOnCLick);
        daycv.setOnClickListener(daycvOnCLick);
        weekcv.setOnClickListener(weekcvOnCLick);
        monthcv.setOnClickListener(monthcvOnCLick);
        yearcv.setOnClickListener(yearcvOnClick);
        allTransactionstv.setOnClickListener(allTransactionsOnClick);
        emptyTransactions.setOnClickListener(emptyOnClick);
        moreOptionsiv.setOnClickListener(cardMoreOnClick);

        optionTodaytv = homeView.findViewById(R.id.option_today);
        optionWeektv = homeView.findViewById(R.id.option_week);
        optionMonthtv = homeView.findViewById(R.id.option_month);
        optionYeartv = homeView.findViewById(R.id.option_year);
        final TextView cardTitle = homeView.findViewById(R.id.today_title_tv);
        final TextView dateSpan = homeView.findViewById(R.id.date_span_tv);

        cardOptionsll = homeView.findViewById(R.id.card_option_ll);
        Date c = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final String d = df.format(c.getTime());

        optionTodaytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOptionsll.setVisibility(GONE);
                cardTitle.setText("Today");
                dateSpan.setText(d.split("/")[0]);
                updateStats("day");
            }
        });

        optionMonthtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOptionsll.setVisibility(GONE);
                cardTitle.setText("This month");
                dateSpan.setText(d.split("/")[1]);
                updateStats("this month");
            }
        });

        optionWeektv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOptionsll.setVisibility(GONE);
                cardTitle.setText("This week");
                dateSpan.setText("this week");
                updateStats("this week");
            }
        });

        optionYeartv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOptionsll.setVisibility(GONE);
                cardTitle.setText("This year");
                dateSpan.setText(d.split("/")[2]);
                updateStats("this year");
            }
        });


        initRecyclerView();
        updateList(MainActivity.getCurrentAccount());
        updateStats(titleCardtv.getText().toString().toLowerCase());


        String accountSelected = MainActivity.getCurrentAccount();

        loaderArgs.putString("selectedAccount", accountSelected);



        Log.v("FragmentHome", "onCreate is done" + accountSelected);
        return homeView;
    }

    private void initRecyclerView() {

        transactionslv.setNestedScrollingEnabled(false);
        transactionslv.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(MainActivity.getContextOfApplication(), LinearLayoutManager.VERTICAL, false);
        transactionslv.setLayoutManager(layoutManager);

        transactionListAdapter = new TransactionListAdapter(transactionList, "home");
        transactionslv.setAdapter(transactionListAdapter);
    }


    private void updateStats(String timeline) {
        fabLayer.setVisibility(GONE);
        SimpleDateFormat df;
        Date c;
        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("yyyy-MM-dd");
        String d = df.format(c.getTime());
        Log.v("-*-*-*-", "*-*-*-*-*\n\n" + d);
        String todaysDate = d.split("-")[2], todaysMonth = d.split("-")[1], todaysYear = d.split("-")[0];

        float yearExpense = 0, monthExpense = 0, weekExpense = 0, dayExpense = 0, yearIncome = 0, monthIncome = 0, weekIncome = 0, dayIncome = 0;
        String[] projection = {TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT, TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_DATE, TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE};
        String selection = null;
        String[] selectionArgs = null;
        Cursor allTransactioncursor = getActivity().getContentResolver().query(TransactionsContract.TransactionsEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        while(allTransactioncursor.moveToNext()){
            String type = allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE));
            String date = allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_DATE));
            String[] dateArr = date.split("-");
            if(dateArr[0].equals(todaysYear)){
                if("I".equals(type))
                    yearIncome += Float.parseFloat(allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT)));
                else
                    yearExpense += Float.parseFloat(allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT)));
                if(dateArr[1].equals(todaysMonth)){
                    if ("I".equals(type))
                        monthIncome += Float.parseFloat(allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT)));
                    else
                        monthExpense += Float.parseFloat(allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT)));

                    if(isDateInCurrentWeek(dateArr[2])){
                        if("I".equals(type))
                            weekIncome += Float.parseFloat(allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT)));
                        else
                            weekExpense += Float.parseFloat(allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT)));

                        if(dateArr[2].equals(todaysDate)){
                            if("I".equals(type))
                                dayIncome += Float.parseFloat(allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT)));
                            else
                                dayExpense += Float.parseFloat(allTransactioncursor.getString(allTransactioncursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT)));
                        }
                    }
                }
            }
        }
        todayRedtv.setText(dayExpense+"");
        weekRedtv.setText(weekExpense+"");
        monthRedtv.setText(monthExpense+"");
        yearRedtv.setText(yearExpense+"");
        todayGreentv.setText(dayIncome+"");
        weekGreentv.setText(weekIncome+"");
        monthGreentv.setText(monthIncome+"");
        yearGreentv.setText(yearIncome+"");

        isDateInCurrentWeek("15");

        if(timeline.equals("this year")) {
            amountCardtv.setText(NumberFormat.getCurrencyInstance().format(yearIncome - yearExpense));
            dateSpantv.setText("Year " + todaysYear);
        } else if(timeline.equals("this month")) {
            amountCardtv.setText(NumberFormat.getCurrencyInstance().format(monthIncome - monthExpense));
            dateSpantv.setText(todaysMonth + "/" + todaysYear);
        } else if(timeline.equals("this week")) {
            amountCardtv.setText(NumberFormat.getCurrencyInstance().format(weekIncome - weekExpense));
            String week = currentWeekDays.get(0) + "/" + currentWeekMonth.get(0) + " - " + currentWeekDays.get(6) + "/" + currentWeekMonth.get(6);
            dateSpantv.setText(week);
        } else {
            amountCardtv.setText(NumberFormat.getCurrencyInstance().format(dayIncome - dayExpense));
            dateSpantv.setText(todaysDate + "/" + todaysMonth);
        }

    }


    public static void updateList(String accountSelected){
        transactionList.clear();

        String[] projection = {TransactionsContract.TransactionsEntry._ID,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_CATEGORY,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_SUB_CATEGORY,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_FUND,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_DATE,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE,};


        String sortOrder = _ID+" DESC";
        String selection = TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT + "=?";
        String[] selectionArgs = {""};
        if(accountSelected.equals("All accounts")) {
            selection = null;
            selectionArgs = null;
        }
        else {
            selectionArgs[0] = accountSelected;
        }
        Cursor transactionCursor = MainActivity.getContextOfApplication().getContentResolver().query(TransactionsContract.TransactionsEntry.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        if(transactionCursor != null){
            while (transactionCursor.moveToNext()){
                String amount = transactionCursor.getString(transactionCursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT));
                String account = transactionCursor.getString(transactionCursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT));
                String fund = transactionCursor.getString(transactionCursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_FUND));
                String category = transactionCursor.getString(transactionCursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_CATEGORY));
                String subCategory = transactionCursor.getString(transactionCursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_SUB_CATEGORY));
                String date = transactionCursor.getString(transactionCursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_DATE));
                String type = transactionCursor.getString(transactionCursor.getColumnIndexOrThrow(TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE));
                TransactionObject transaction;
                if("I".equals(type)) transaction = new TransactionObject(account, amount, fund, date, type);
                else transaction = new TransactionObject(subCategory, amount, fund, date, type);
                if(!transactionList.contains(transaction)) transactionList.add(transaction);
            }
        }
        if(transactionList.size() < 1) emptyTransactions.setVisibility(View.VISIBLE);
        else emptyTransactions.setVisibility(GONE);

        transactionListAdapter.notifyDataSetChanged();
    }


    private boolean isDateInCurrentWeek(String s) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        currentWeekDays.clear();
        currentWeekMonth.clear();

        for(int i = 0; i <= 6; i++){
            c.add(Calendar.DATE, 1);
            String first = df.format(c.getTime()).split("-")[2];
            String m = df.format(c.getTime()).split("-")[1];
            currentWeekMonth.add(m);
            currentWeekDays.add(first);
        }

        if(currentWeekDays.contains(s))
            return true;
        else
            return false;
    }

    private void hideFabs(){
        fabLayer.setVisibility(GONE);
        expenseFab.hide();
        incomeFab.hide();
        transferFab.hide();
        budgetFab.hide();

        addFab.startAnimation(fabOpen);

        expensetv.setVisibility(View.INVISIBLE);
        incometv.setVisibility(View.INVISIBLE);
        transfertv.setVisibility(View.INVISIBLE);
        budgettv.setVisibility(GONE);

        isfabOpen = false;
    }

    private void showFabs(){
        fabLayer.setVisibility(View.VISIBLE);
        expenseFab.show();
        incomeFab.show();
        transferFab.show();
        budgetFab.show();

        addFab.startAnimation(fabClose);

        expensetv.setVisibility(View.VISIBLE);
        incometv.setVisibility(View.VISIBLE);
        transfertv.setVisibility(View.VISIBLE);
        budgettv.setVisibility(View.VISIBLE);

        isfabOpen = true;
    }

  /*  @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {TransactionsContract.TransactionsEntry._ID,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_AMOUNT,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_CATEGORY,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_FUND,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_DATE,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TYPE,
                TransactionsContract.TransactionsEntry.COLUMN_TRANSACTION_TAG,};


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

        Log.v("FragmentHome", "onCreateLoader is done");

        return new CursorLoader(getContext(), TransactionsContract.TransactionsEntry.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        transactionsCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        transactionsCursorAdapter.swapCursor(null);
    }*/

    @Override
    public void onResume() {
        hideFabs();
        updateList(MainActivity.getCurrentAccount());
        //updateStats();
        super.onResume();
    }
}
