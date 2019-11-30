package com.example.myidol.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseActivity<B extends ViewDataBinding,VM extends BaseViewmodel> extends AppCompatActivity {
    protected B binding;
    protected VM viewmodel;
    public abstract Class<VM> getViewmodel();
    public abstract int getLayoutID();
    public abstract void setBindingViewmodel();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,getLayoutID());
        viewmodel = ViewModelProviders.of(this).get(getViewmodel());
        setBindingViewmodel();
    }
    public void gotoActivity(AppCompatActivity activity,Bundle bundle){
        Intent intent = new Intent(this,activity.getClass());
        if(bundle!=null){
            intent.putExtra("bundle",bundle);
        }
        startActivity(intent);
    }
}
