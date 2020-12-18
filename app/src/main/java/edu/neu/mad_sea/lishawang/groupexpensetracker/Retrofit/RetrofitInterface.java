package edu.neu.mad_sea.lishawang.groupexpensetracker.Retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @GET("v6/08bdcfd8ae90dc17dc174912/latest/{currency}")
    Call<JsonObject> getExchangeCurrency(@Path("currency") String currency);
}
