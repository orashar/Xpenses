package com.example.android.xpenses;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.xpenses.Adapters.CategoryListAdapter;
import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.example.android.xpenses.DataTypes.Category;
import com.example.android.xpenses.DataTypes.SubCategory;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private ArrayList<Category> categoryList;
    private CategoryListAdapter cfListAdapter;
    private ExpandableListView listc;

    private SearchView searchcf;

    private String subCategory, category;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryList = new ArrayList<>();
        loadListDatac();

        listc = findViewById(R.id.list_c);
        cfListAdapter = new CategoryListAdapter(this, categoryList);
        listc.setAdapter(cfListAdapter);

        final TextView suggestion = findViewById(R.id.suggested_category_tv);
        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] suggArr = suggestion.getText().toString().split("-");
                done(suggArr[1], suggArr[0]);
            }
        });

        searchcf = findViewById(R.id.search_c);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchcf.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchcf.setIconifiedByDefault(false);
        searchcf.setOnQueryTextListener(this);
        searchcf.setOnCloseListener(this);

        listc.setGroupIndicator(null);

        listc.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                subCategory = categoryList.get(groupPosition).getSubCategoryList().get(childPosition).getName();
                category = categoryList.get(groupPosition).getName();
                done(subCategory, category);
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void done(String s, String c){
        Intent mainIntent = new Intent();
        mainIntent.putExtra("CATEGORY_AND_SUBCATEGORY", new String[] {c, s});
        setResult(RESULT_OK, mainIntent);
        finish();
    }


    private void collapseAll(){
        int count = cfListAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            listc.collapseGroup(i);
        }
    }


    private void expandAll() {
        int count = cfListAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            listc.expandGroup(i);
        }
    }


    private void loadListDatac(){
        String[] projection = {TransactionsContract.CategoryEntry.COLUMN_CATEGORY, TransactionsContract.CategoryEntry.COLUMN_SUB_CATEGORY};
        String sortOrder = TransactionsContract.CategoryEntry.COLUMN_CATEGORY + " COLLATE NOCASE";
        Cursor c = getContentResolver().query(TransactionsContract.CategoryEntry.CONTENT_URI_CATEGORY, projection, null, null, sortOrder);

        if(c != null && c.moveToFirst()) {
            SubCategory subCategory;
            ArrayList<SubCategory> subCategoryList = new ArrayList<>();

            String curr;
            String currSub;
            String next = c.getString(c.getColumnIndexOrThrow(TransactionsContract.CategoryEntry.COLUMN_CATEGORY));
            String nextSub = c.getString(c.getColumnIndexOrThrow(TransactionsContract.CategoryEntry.COLUMN_SUB_CATEGORY));

            while (c.moveToNext()){
                curr = next;
                currSub = nextSub;
                next = c.getString(c.getColumnIndexOrThrow(TransactionsContract.CategoryEntry.COLUMN_CATEGORY));
                nextSub = c.getString(c.getColumnIndexOrThrow(TransactionsContract.CategoryEntry.COLUMN_SUB_CATEGORY));

                subCategory = new SubCategory(currSub);
                subCategoryList.add(subCategory);

                if(!(curr.equals(next))){
                    Category category = new Category(curr, subCategoryList);
                    categoryList.add(category);
                    subCategoryList = new ArrayList<>();
                }

            }
        }
    }

    @Override
    public boolean onClose() {
        cfListAdapter.filterData("");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        cfListAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        cfListAdapter.filterData(query);
        expandAll();
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            /*case R.id.edit_category:
                Toast.makeText(this, "Add Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_all:
                Toast.makeText(this, "delete all", Toast.LENGTH_SHORT).show();
                return true;*/
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
