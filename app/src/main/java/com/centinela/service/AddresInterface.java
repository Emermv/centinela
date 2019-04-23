package com.centinela.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AddresInterface {
  /*  @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })*/
    @GET("users/{args}")
    Call<String> getDirection(@Path("args") String args);
}
