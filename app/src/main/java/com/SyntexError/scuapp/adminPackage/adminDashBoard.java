package com.SyntexError.scuapp.adminPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.SyntexError.scuapp.R;
import com.SyntexError.scuapp.loginAcitviy;
import com.google.android.material.tabs.TabLayout;


public class adminDashBoard extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        viewPager = findViewById(R.id.viewPagerID);
        tabLayout = (TabLayout) findViewById(R.id.TablaoutId);

        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());
        //adding Fragments
        adapter.AddFragment(new productListFragment(), "All Item");
        adapter.AddFragment(new runningBookingFragment(), "Running Request");
        adapter.AddFragment(new pendingRequestFragment(), "Pending Request");
        adapter.AddFragment(new accpetedRequestFragment(), "All Booking");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOUt:
                // do something
                // do something
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username" , "null") ;
                editor.putString("password", "null");
                editor.putString("state","null") ;
                editor.apply();
                editor.commit();

                Intent i = new Intent(getApplicationContext() , loginAcitviy.class);
                startActivity(i);
                finish();

                return true;

            default:
                return super.onContextItemSelected(item);
        }


    }


}


