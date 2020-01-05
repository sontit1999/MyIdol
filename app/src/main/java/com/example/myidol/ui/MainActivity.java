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
import com.example.myidol.fragment.profile.FragmentProfileUser;
import com.example.myidol.fragment.search.FragmentSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                        loadFragment(new FragmentHome());
                        break;
                    case R.id.nav_hot:
                        loadFragment(new FragmentSearch());
                        break;
                    case R.id.nav_add:
                        loadFragment(new FragmentAdd());
                        break;
                    case R.id.nav_favorite:
                        loadFragment(new FragmentFavorite());
                        break;
                    case R.id.nav_user:
                        loadFragment(new FragmentProfileUser());
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
