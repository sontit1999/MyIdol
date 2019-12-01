package com.example.myidol.callback;

import com.example.myidol.model.IdolHot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestAPI {
    @GET("getidol.php/")
    Call<List<IdolHot>> getIdol();
}
