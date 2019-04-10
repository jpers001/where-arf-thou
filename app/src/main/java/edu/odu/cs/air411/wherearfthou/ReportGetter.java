package edu.odu.cs.air411.wherearfthou;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ReportGetter extends AsyncTask<Void, Integer, ArrayList<ReportData> >{
    public interface ReportGetterListener {
        void onReportGetterCompleted(ArrayList<ReportData> reports);
        void onReportGetterError(String error);
    }

    ArrayList<ReportData> reportsFromJson = new ArrayList<>();
    private final ReportGetterListener rListener;

    public ReportGetter(ReportGetterListener listener)
    {
        rListener = listener;
    }


    protected void onPreExecute()
    {

    }

    protected ArrayList<ReportData> doInBackground(Void... params)
    {
        //ArrayList<ReportData> reportsFromJson= new ArrayList<>();
        try{
            reportsFromJson = GettingReports();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return reportsFromJson;
        }

    }

    protected void onProgressUpdate()
    {

    }

    protected void onPostExecute(ArrayList<ReportData> reportsFromJson)
    {
        if(rListener != null)
        {
            rListener.onReportGetterCompleted(reportsFromJson);
        }
    }

    public static String getNullAsEmptyString(JsonElement jsonElement) {
        if(jsonElement.isJsonNull())
            return "";
        else
            return jsonElement.getAsString();
    }

    public static ArrayList<ReportData> GettingReports() throws IOException
    {
        //URL to access report data in JSON format
        ArrayList<ReportData> reports = new ArrayList<>();
        URL url = new URL("http://wherearfthou.duckdns.org/wherearf/db/v1/Api.php?apicall=getreports");
        //URL url = new URL("https://api.myjson.com/bins/iighs");
        URLConnection request = url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

        if (root instanceof JsonObject) {
            JsonObject jobject = root.getAsJsonObject();
            JsonArray jarray = jobject.get("reports").getAsJsonArray();

            for(int i = 0; i < jarray.size(); i++)
            {
                JsonObject currentJsonObject = jarray.get(i).getAsJsonObject();

                if(currentJsonObject.isJsonNull() != true)
                {
                    ReportData currentReport = new ReportData();
                    currentReport.setName(getNullAsEmptyString(currentJsonObject.get("pet_name")));
                    currentReport.setImage(getNullAsEmptyString(currentJsonObject.get("photo")));
                    currentReport.setLocation(getNullAsEmptyString(currentJsonObject.get("location")));
                    currentReport.setReportDate(getNullAsEmptyString(currentJsonObject.get("last_seen")));
                    currentReport.setDescription(getNullAsEmptyString(currentJsonObject.get("description")));
                    currentReport.setContact(getNullAsEmptyString(currentJsonObject.get("contact")));
                    reports.add(currentReport);
                }
            }
        }

        return reports;
    }
}
