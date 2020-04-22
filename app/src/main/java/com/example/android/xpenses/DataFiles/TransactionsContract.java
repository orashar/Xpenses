package com.example.android.xpenses.DataFiles;

import android.net.Uri;
import android.provider.BaseColumns;

public class TransactionsContract {

    public static final String CONTENT_AUTHORIY = "com.example.android.xpenses";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORIY);
    public static final String PATH_TRANSACTIONS = "transactions";
    public static final String PATH_CATEGORY = "categories";
    public static final String PATH_ACCOUNT = "accounts";
    public static final String PATH_FUND = "funds";
    public static final String PATH_TRANSFERS = "transfers";
    public static final String PATH_BUDGET = "budget";

    private TransactionsContract(){}

    public static final class TransactionsEntry{
        public static final String TABLE_NAME = "transactions";

        public static final String _ID = "_id";
        public static final String COLUMN_TRANSACTION_AMOUNT = "transaction_amount";
        public static final String COLUMN_TRANSACTION_CATEGORY = "transaction_category";
        public static final String COLUMN_TRANSACTION_SUB_CATEGORY = "transaction_sub_category";
        public static final String COLUMN_TRANSACTION_ACCOUNT = "transaction_account";
        public static final String COLUMN_TRANSACTION_FUND = "transaction_fund";
        public static final String COLUMN_TRANSACTION_NOTE = "transaction_note";
        public static final String COLUMN_TRANSACTION_TYPE = "transaction_type";
        public static final String COLUMN_TRANSACTION_DATE = "transaction_date";
        public static final String COLUMN_TRANSACTION_TIME = "transaction_time";
        public static final String COLUMN_TRANSACTION_PAYEE = "transaction_payee";
        public static final String COLUMN_TRANSACTION_SOURCE = "transaction_source";
        public static final String COLUMN_TRANSACTION_PURPOSE = "transaction_purpose";
        public static final String COLUMN_TRANSACTION_TAG = "transaction_tag";
        public static final String COLUMN_IN_BUDGET = "is_budget";
        public static final String COLUMN_IN_REFUND = "is_refund";


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TRANSACTIONS);
    }

    public static final class CategoryEntry{
        public static final String CATEGORY_TABLE_NAME = "categories";

        public static final String CID = "cid";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SUB_CATEGORY = "sub_category";
        public static final String COLUMN_CATEGORY_BUDGET = "category_budget";
        public static final String COLUMN_CATEGORY_EXPENSE = "category_expense";


        public static final Uri CONTENT_URI_CATEGORY = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORY);
    }

    public static final class AccountsEntry{
        public static final String ACCOUNTS_TABLE_NAME = "accounts";

        public static final String AID = "aid";
        public static final String COLUMN_ACCOUNT = "account";
        public static final String COLUMN_FID = "fid";
        public static final String COLUMN_FUND = "fund";

        public static final Uri CONTENT_URI_ACCOUNTS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACCOUNT);
    }

    public static final class FundEntry{
        public static final String FUND_TABLE_NAME = "funds";

        public static final String COLUMN_FID = "fid";
        public static final String COLUMN_ACCOUNT = "account";
        public static final String COLUMN_FUND = "fund";
        public static final String COLUMN_MONEY_SPENT = "money_spent";
        public static final String COLUMN_INITIAL_BALANCE = "initial_bal";
        public static final String COLUMN_FUND_BUDGET = "fund_budget";
        public static final String COLUMN_FUND_DESC = "fund_desc";
        public static final String COLUMN_FUND_TYPE = "fund_type";

        public static final Uri CONTENT_URI_FUND = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FUND);
    }

    public static final class TransferEntry{
        public static final String TRANSFER_TABLE_NAME = "transfers";

        public static final String TID = "tid";
        public static final String COLUMN_TRANSFER_AMMOUNT = "transfer_amount";
        public static final String COLUMN_ACCOUNT_TO = "account_to";
        public static final String COLUMN_ACCOUNT_FROM = "account_from";
        public static final String COLUMN_TRANSFER_DESC = "transfer_desc";
        public static final String COLUMN_FUND_TO = "fund_to";
        public static final String COLUMN_FUND_FROM = "fund_from";

        public static final Uri CONTENT_URI_TRANSFERS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TRANSFERS);
    }

    public static final class BudgetEntry{
        public static final String BUDGET_TABLE_NAME = "budget";

        public static final String BID = "tid";
        public static final String COLUMN_BUDGET_AMOUNT = "budget_amount";
        public static final String COLUMN_BUDGET_ACCOUNT = "budget_account";
        public static final String COLUMN_BUDGET_FUND = "budget_fund";
        public static final String COLUMN_BUDGET_CATEGORY = "budget_category";
        public static final String COLUMN_BUDGET_SUB_CATEGORY = "budget_sub_category";
        public static final String COLUMN_BUDGET_START_DATE = "budget_start_date";
        public static final String COLUMN_BUDGET_END_DATE = "budget_end_date";
        public static final String COLUMN_BUDGET_NOTE = "budget_note";

        public static final Uri CONTENT_URI_BUDGET = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BUDGET);
    }
}