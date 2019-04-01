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


}


