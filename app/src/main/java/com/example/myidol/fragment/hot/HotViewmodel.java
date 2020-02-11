package com.example.myidol.fragment.hot;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myidol.adapter.IdolAdapter;
import com.example.myidol.adapter.PhotoAdapter;
import com.example.myidol.base.BaseViewmodel;
import com.example.myidol.callback.RequestAPI;
import com.example.myidol.model.APIClient;
import com.example.myidol.model.IdolHot;
import com.example.myidol.model.Photo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotViewmodel extends BaseViewmodel {
    IdolAdapter adapter = new IdolAdapter();
    MutableLiveData<ArrayList<IdolHot>> arrayIDol = new MutableLiveData<>();
    public HotViewmodel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArrayList<IdolHot>> getArrayIDol(){
        // call api
        Retrofit retrofit = APIClient.getClient("http://sonhaui.000webhostapp.com/");
        RequestAPI callapi  = retrofit.create(RequestAPI.class);
        Call<List<IdolHot>> call = callapi.getIdol();
        call.enqueue(new Callback<List<IdolHot>>() {
            @Override
            public void onResponse(Call<List<IdolHot>> call, Response<List<IdolHot>> response) {
                Log.d("test","số lượng: " + response.body().size());
                arrayIDol.postValue((ArrayList<IdolHot>) response.body());
            }

            @Override
            public void onFailure(Call<List<IdolHot>> call, Throwable t) {
                Log.d("test",t.getMessage().toString());
            }
        });

        return arrayIDol;
    }
    // get thêm idol
    public void getmoreIdol(){
        Retrofit retrofit = APIClient.getClient("http://sonhaui.000webhostapp.com/");
        RequestAPI callapi  = retrofit.create(RequestAPI.class);
        Call<List<IdolHot>> call = callapi.getIdol();
        call.enqueue(new Callback<List<IdolHot>>() {
            @Override
            public void onResponse(Call<List<IdolHot>> call, Response<List<IdolHot>> response) {
                Log.d("test","Lấy thêm: " + response.body().size());
                ArrayList<IdolHot> arraycu  = arrayIDol.getValue();
                arraycu.addAll(response.body());
                arrayIDol.postValue(arraycu);
            }

            @Override
            public void onFailure(Call<List<IdolHot>> call, Throwable t) {
                Log.d("test",t.getMessage().toString());
            }
        });
    }
    public void showProgress(ProgressBar pg){
        pg.setVisibility(View.VISIBLE);
    }
    public void hiddenProgress(ProgressBar pg){
       pg.setVisibility(View.GONE);
    }
}
