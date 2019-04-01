package edu.odu.cs.air411.wherearfthou;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    //ip for accessing localhost using retrofit
    public static final String BASE_URL= "http://wherearfthou.duckdns.org/wherearf/";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient()
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}