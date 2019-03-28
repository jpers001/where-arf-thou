package helper;

public class Api {
    public static final String ROOT_URL = "http://wherearfthou.duckdns.org/wherearf/db/v1/Api.php?apicall=";

    public static final String URL_CREATE_HERO = ROOT_URL + "createreport";
    public static final String URL_READ_HEROES = ROOT_URL + "getreports";
    // Below not implemented yet
    public static final String URL_UPDATE_HERO = ROOT_URL + "updatereport";
    public static final String URL_DELETE_HERO = ROOT_URL + "deletereport&id=";
}
