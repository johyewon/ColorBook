package com.hanix.colorbook.task.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanix.nativebaseproject.common.constants.URLApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 레트로핏 빌더
 */
public class MyRetrofit2 {

    static Retrofit mRetrofit;

    public static Retrofit getmRetrofit() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logger);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(URLApi.getServerUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        return mRetrofit;
    }

}
