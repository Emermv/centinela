package com.centinela.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddresService  {
    public void find(double lat,double lng, Callback callback){

        /*OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://geocode.xyz/"+lat+","+lng+"?geoit=json")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache").build();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) { e.printStackTrace(); }
            @Override public void onResponse(Call call, final Response response) throws IOException {
                Log.d("AMOR","Unexpected code " + response);

                if (!response.isSuccessful()) { throw new IOException("Unexpected code " + response); } else {
                    String cadenaJson = response.body().string();
                    Log.i("AMOR", cadenaJson);
                    Gson gson = new Gson();

                    Type stringStringMap = new TypeToken<ArrayList<Map< String, Object >>>() {}.getType();
                    final ArrayList < Map < String, Object >> retorno = gson.fromJson(cadenaJson, stringStringMap);


                    for (Map < String, Object > x: retorno) {
                        Log.d("AMOR",x.get("country").toString()+" "+x.get("city").toString()+" "+x.get("staddress").toString());

                        break;

                    }

                }
            }
        });

        /*OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "undefined=");
        Request request = new Request.Builder()
                .url("https://geocode.xyz/-12.1852706,-76.9906658?geoit=json")
                .get()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")

                .build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String cadenaJson = response.body().string();
                Log.d("AMOR", cadenaJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://geocode.xyz/")
                .build();

        AddresInterface service = retrofit.create(AddresInterface.class);
        Call<String> s=service.getDirection("-12.1852706,-76.9906658?geoit=json");
        try {
           Response<String> response= s.execute();
           Log.d("AMOR",response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
