package com.tptien.tpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tptien.tpractice.Adapter.ViewPagerAdapter;
import com.tptien.tpractice.Fragment.FavoriteFragment;
import com.tptien.tpractice.Fragment.NewestFragment;
import com.tptien.tpractice.Fragment.YourSelfFragment;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab_createTest;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private ViewPager mViewPager;
    private BottomNavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar=(Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        mSharedPreferences =getSharedPreferences("loginAccount",MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
        fab_createTest=(FloatingActionButton)findViewById(R.id.fab_createTest);
        fab_createTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CreateTestActivity.class);
                startActivity(intent);
            }
        });
        //
        mViewPager =(ViewPager)findViewById(R.id.viewPager_main);
        mViewPager.setCurrentItem(mSharedPreferences.getInt("currentPage",0));
        Log.d("currentpage", String.valueOf(mSharedPreferences.getInt("currentPage",0))) ;
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //
        mNavigationView= (BottomNavigationView)findViewById(R.id.bottomNav);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_new:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.item_favorite:
                        mViewPager.setCurrentItem(1);
                        break;
//                    case R.id.item_yourSelf:
//                        mViewPager.setCurrentItem(2);
//                        break;
                }

                return false;

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Log.d("logout","click");
                mEditor.putBoolean("saveAccount",false);
                mEditor.apply();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getResources().getString(R.string.main_alert_quit));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.all_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.all_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    protected void onPause() {
        mEditor.putInt("currentPage",mViewPager.getCurrentItem());
        mEditor.apply();
        super.onPause();

    }
}