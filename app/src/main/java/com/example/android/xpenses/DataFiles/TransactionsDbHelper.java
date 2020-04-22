package com.example.android.xpenses.DataFiles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.xpenses.DataFiles.TransactionsContract.TransactionsEntry;
import com.example.android.xpenses.DataFiles.TransactionsContract.CategoryEntry;
import com.example.android.xpenses.DataFiles.TransactionsContract.AccountsEntry;
import com.example.android.xpenses.DataFiles.TransactionsContract.FundEntry;
import com.example.android.xpenses.DataFiles.TransactionsContract.TransferEntry;
import com.example.android.xpenses.DataFiles.TransactionsContract.BudgetEntry;
import com.example.android.xpenses.R;

import java.util.Scanner;

public class TransactionsDbHelper extends SQLiteOpenHelper {

    Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "xpenses.db";
    public static final String SQL_CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TransactionsEntry.TABLE_NAME + "("
            + TransactionsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TransactionsEntry.COLUMN_TRANSACTION_AMOUNT + " TEXT NOT NULL, "
            + TransactionsEntry.COLUMN_TRANSACTION_CATEGORY + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_SUB_CATEGORY + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_ACCOUNT + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_FUND + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_NOTE + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_TYPE + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_DATE + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_TIME + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_PAYEE + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_SOURCE + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_PURPOSE + " TEXT, "
            + TransactionsEntry.COLUMN_TRANSACTION_TAG + " TEXT, "
            + TransactionsEntry.COLUMN_IN_BUDGET + " INTEGER, "
            + TransactionsEntry.COLUMN_IN_REFUND + " INTEGER);";

    public static final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " + CategoryEntry.CATEGORY_TABLE_NAME + "("
            + CategoryEntry.CID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CategoryEntry.COLUMN_CATEGORY + " TEXT, "
            + CategoryEntry.COLUMN_CATEGORY_BUDGET + " TEXT, "
            + CategoryEntry.COLUMN_CATEGORY_EXPENSE + " TEXT, "
            + CategoryEntry.COLUMN_SUB_CATEGORY + " TEXT);";

    public static final String SQL_CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + AccountsEntry.ACCOUNTS_TABLE_NAME + "("
            + AccountsEntry.AID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AccountsEntry.COLUMN_ACCOUNT + " TEXT );";

    public static final String SQL_CREATE_FUNDS_TABLE = "CREATE TABLE " + TransactionsContract.FundEntry.FUND_TABLE_NAME + "("
            + FundEntry.COLUMN_FID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FundEntry.COLUMN_ACCOUNT + " TEXT, "
            + FundEntry.COLUMN_FUND_TYPE + " TEXT, "
            + FundEntry.COLUMN_INITIAL_BALANCE + " TEXT, "
            + FundEntry.COLUMN_MONEY_SPENT + " TEXT, "
            + FundEntry.COLUMN_FUND_BUDGET + " TEXT, "
            + FundEntry.COLUMN_FUND_DESC + " TEXT, "
            + AccountsEntry.COLUMN_FUND + " TEXT);";

    public static final String SQL_CREATE_TRANSFERS_TABLE = "CREATE TABLE " + TransferEntry.TRANSFER_TABLE_NAME + "("
            + TransferEntry.TID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TransferEntry.COLUMN_TRANSFER_AMMOUNT + " TEXT, "
            + TransferEntry.COLUMN_ACCOUNT_FROM + " TEXT, "
            + TransferEntry.COLUMN_ACCOUNT_TO + " TEXT, "
            + TransferEntry.COLUMN_FUND_FROM + " TEXT, "
            + TransferEntry.COLUMN_FUND_TO + " TEXT, "
            + TransferEntry.COLUMN_TRANSFER_DESC + " TEXT);";

    public static final String SQL_CREATE_BUDGET_TABLE = "CREATE TABLE " + BudgetEntry.BUDGET_TABLE_NAME + "("
            + BudgetEntry.BID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BudgetEntry.COLUMN_BUDGET_AMOUNT + " TEXT, "
            + BudgetEntry.COLUMN_BUDGET_ACCOUNT + " TEXT, "
            + BudgetEntry.COLUMN_BUDGET_FUND + " TEXT, "
            + BudgetEntry.COLUMN_BUDGET_CATEGORY + " TEXT, "
            + BudgetEntry.COLUMN_BUDGET_SUB_CATEGORY + " TEXT, "
            + BudgetEntry.COLUMN_BUDGET_START_DATE + " TEXT, "
            + BudgetEntry.COLUMN_BUDGET_NOTE + " TEXT, "
            + BudgetEntry.COLUMN_BUDGET_END_DATE + " TEXT);";

    private static final String SQL_DELETE_ENTRIES = "";


    public TransactionsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TRANSACTIONS_TABLE);
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_ACCOUNTS_TABLE);
        db.execSQL(SQL_CREATE_FUNDS_TABLE);
        db.execSQL(SQL_CREATE_TRANSFERS_TABLE);
        db.execSQL(SQL_CREATE_BUDGET_TABLE);

        insertCategories(db);
        insertAccounts(db);
        insertFunds(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void insertCategories(SQLiteDatabase db){
        Scanner sc = new Scanner(this.context.getResources().openRawResource(R.raw.categories));

        while (sc.hasNextLine()){
            String curr = sc.nextLine();
            String[] currArr = curr.split(",");

            db.execSQL("INSERT INTO " + CategoryEntry.CATEGORY_TABLE_NAME + "("  + CategoryEntry.COLUMN_CATEGORY + " , " + CategoryEntry.COLUMN_SUB_CATEGORY + ") VALUES('" + currArr[1] + "' , '" + currArr[2] + "');");
        }
    }

    private void insertAccounts(SQLiteDatabase db) {
        Scanner sc = new Scanner(this.context.getResources().openRawResource(R.raw.accounts));

        while (sc.hasNextLine()){
            String curr = sc.nextLine();
            String[] currArr = curr.split(",");

            db.execSQL("INSERT INTO " + AccountsEntry.ACCOUNTS_TABLE_NAME + "("  + AccountsEntry.COLUMN_ACCOUNT + ") VALUES('" + currArr[1] + "');");
        }
    }

    private void insertFunds(SQLiteDatabase db) {
        Scanner sc = new Scanner(this.context.getResources().openRawResource(R.raw.funds));

        while (sc.hasNextLine()){
            String curr = sc.nextLine();
            String[] currArr = curr.split(",");

            db.execSQL("INSERT INTO " + FundEntry.FUND_TABLE_NAME + "("  + FundEntry.COLUMN_FUND + " , " + FundEntry.COLUMN_ACCOUNT + " , " + FundEntry.COLUMN_FUND_TYPE + " , " + FundEntry.COLUMN_INITIAL_BALANCE + " , " + FundEntry.COLUMN_MONEY_SPENT + ") VALUES('" + currArr[1] + "' , '" + currArr[2] + "' , '" + currArr[3] + "' , '" + currArr[4] + "' , '" + currArr[5] + "');");
        }
    }

}
