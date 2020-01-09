package com.example.myidol.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerChatAdapter extends FragmentPagerAdapter {
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<Fragment> listFrag = new ArrayList<>();
    public ViewPagerChatAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFrag.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
    public void addFragment(Fragment fragment,String tittle){
        listFrag.add(fragment);
        titles.add(tittle);
    }
}
