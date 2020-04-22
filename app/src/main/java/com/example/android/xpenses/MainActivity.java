package com.example.android.xpenses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.xpenses.DataFiles.TransactionsContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;
    private BottomNavigationView bottomNavigationView;
    public static Spinner accountSpinner;
    static ArrayList<String> accountList;
    ArrayAdapter<String> accountAdapter;


    public static Context contextOfApplication;
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MainActivity", "Setting Content View");
        setContentView(R.layout.activity_main);

        Log.v("MainActivity", "Content View is set");
        init();

        Log.v("MainActivity", "init is done");

        contextOfApplication = getApplicationContext();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        Log.v("MainActivity", "onCreate is done");
    }

    private void init() {

        Log.v("MainActivity", "Starting init");
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        accountSpinner= findViewById(R.id.account_spinner);

        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this,R.id.main);
        appBarConfiguration = new AppBarConfiguration.Builder(new int[]{R.id.home, R.id.funds, R.id.stats})
                .setDrawerLayout(drawer)
                .build();

        loadAccountData();

        accountAdapter = new ArrayAdapter<String>(this, R.layout.account_spinner_item, accountList);
       // accountAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountAdapter);

        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NavDestination currentFragment = NavHostFragment.findNavController(getSupportFragmentManager().getPrimaryNavigationFragment().getFragmentManager().getFragments().get(0)).getCurrentDestination();
                FragmentFunds.updateList(accountSpinner.getSelectedItem().toString());
                FragmentHome.updateList(accountSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ActionBarDrawerToggle toggleActionBar = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggleActionBar);
        toggleActionBar.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.add);
    }

    public static ArrayList<String> getSpinnerList(){
        if(accountList != null) return accountList;
        else return null;
    }

    private void loadAccountData(){
        accountList = new ArrayList<>();
        String[] projection = {TransactionsContract.AccountsEntry.COLUMN_ACCOUNT};
        Cursor c = getContentResolver().query(TransactionsContract.AccountsEntry.CONTENT_URI_ACCOUNTS, projection, null, null, null);

        if(c != null && c.moveToFirst()){
            do{
                String acc = c.getString(c.getColumnIndexOrThrow(TransactionsContract.AccountsEntry.COLUMN_ACCOUNT));
                if(!(accountList.contains(acc))) accountList.add(acc);
            }while (c.moveToNext());

            c.close();
        }
        accountList.add("All accounts");

    }

    public static String getCurrentAccount(){
        if(accountSpinner.getSelectedItem() != null) return accountSpinner.getSelectedItem().toString();
        else return "";
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration );
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option:
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAccountData();
        accountAdapter.clear();
        accountAdapter.addAll(accountList);
        accountAdapter.notifyDataSetChanged();
        FragmentFunds.updateList(getCurrentAccount());
    }
}
