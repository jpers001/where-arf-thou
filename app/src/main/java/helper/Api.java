package helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api
{
    public static final String ROOT_URL = "http://wherearfthou.duckdns.org/wherearf/db/v1/Api.php?apicall=";

    public static final String URL_CREATE_REPORT = ROOT_URL + "createreport";
    public static final String URL_READ_REPORTS = ROOT_URL + "getreports";
    // Below not implemented yet
    public static final String URL_UPDATE_REPORT = ROOT_URL + "updatereport";
    public static final String URL_DELETE_REPORT = ROOT_URL + "deletereport&id=";

    public static final String BASE_URL= "http://10.0.2.2/var/www/html/wherearf";
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

}

