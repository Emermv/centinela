package com.centinela.service;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class AddresService  {

    public  void get(double lat,double lng,JsonHttpResponseHandler handler){
        RequestParams rp = new RequestParams();
        HttpUtils.getByUrl("https://geocode.xyz/"+lat+","+lng+"?geoit=json", rp,handler );
    }


}
