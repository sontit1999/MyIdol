package com.example.myidol.ui;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myidol.R;
import com.example.myidol.base.BaseActivity;
import com.example.myidol.databinding.ActivityMainBinding;
import com.example.myidol.fragment.add.FragmentAdd;
import com.example.myidol.fragment.favorite.FragmentFavorite;
import com.example.myidol.fragment.home.FragmentHome;
import com.example.myidol.fragment.hot.FragmentHot;
import com.example.myidol.fragment.profile.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewmodel>{

    @Override
    public Class<MainViewmodel> getViewmodel() {
        return MainViewmodel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void setBindingViewmodel() {
        binding.setViewmodel(viewmodel);
        // load defaut fragment
        loadFragment(new FragmentHome());
        // set listener bottom nav
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                        loadFragment(new FragmentHome());
                        break;
                    case R.id.nav_hot:
                        Toast.makeText(MainActivity.this, "hot", Toast.LENGTH_SHORT).show();
                        loadFragment(new FragmentHot());
                        break;
                    case R.id.nav_add:
                        Toast.makeText(MainActivity.this, "add", Toast.LENGTH_SHORT).show();
                        loadFragment(new FragmentAdd());
                        break;
                    case R.id.nav_favorite:
                        Toast.makeText(MainActivity.this, "favorite", Toast.LENGTH_SHORT).show();
                        loadFragment(new FragmentFavorite());
                        break;
                    case R.id.nav_user:
                        loadFragment(new FragmentProfile());
                        Toast.makeText(MainActivity.this, "user", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }

        });
    }
    public void loadFragment(Fragment fragment){
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
