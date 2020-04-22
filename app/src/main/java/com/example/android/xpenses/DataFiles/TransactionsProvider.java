package com.example.android.xpenses.DataFiles;

import com.example.android.xpenses.DataFiles.TransactionsContract.TransactionsEntry;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.example.android.xpenses.DataFiles.TransactionsContract.TransactionsEntry._ID;

public class TransactionsProvider extends ContentProvider {

    public static final String LOG_TAG = TransactionsProvider.class.getSimpleName();

    private TransactionsDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TransactionsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = database.query(uri.toString().split("/")[3], projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        Log.e(LOG_TAG, "insert row for " + uri);

        long id = database.insert(uri.toString().split("/")[3], null, values);

        if(id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int d = database.delete(uri.toString().split("/")[3], selection, selectionArgs);
        Log.v("/*/*/*", d+"");
        return d;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int u = database.update(uri.toString().split("/")[3], values, selection, selectionArgs);
        Log.v("/*/*/*", u+"updated");
        return u;
    }
}
