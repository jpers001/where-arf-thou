
package helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String,Void,String>
{
    String regrister_url = "http://wherearfthou.duckdns.org/wherearf/Regrister.php";
    String login_url="http://wherearfthou.duckdns.org/wherearf/Login.php";
    ProgressDialog progressDialog;
    Context context;
    Activity activity;
    AlertDialog.Builder builder;

    public BackgroundTask(Context context)
    {
        this.context = context;
        activity = (Activity) context;
    }


    @Override
    protected void onPreExecute()
    {
       builder=new AlertDialog.Builder(context);
       progressDialog= new ProgressDialog(context);
       progressDialog.setTitle("Please wait..");
       progressDialog.setMessage("Connecting to server...");
       progressDialog.setIndeterminate(true);
       progressDialog.setCancelable(false);
       progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params)
    {
        String method = params[0];

        if (method.equals("Regrister"))
        {
            try
            {
                URL url = new URL(regrister_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String name= params[1];
                String user_name= params[2];
                String user_password= params[3];
                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                        URLEncoder.encode("user_password","UTF-8")+"="+URLEncoder.encode(user_password,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder= new StringBuilder();
                String line = "";
                while((line=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(line+"\n");
                }
                httpURLConnection.disconnect();
                Thread.sleep(5000);
                return stringBuilder.toString().trim();
            }

            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            catch (IOException e)
            {
                e.printStackTrace();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }


        }
        else if (method.equals("login"))
        {
            try
            {
                URL url = new URL(login_url);
            }
            catch(MalformedURLException e)
                {
                    e.printStackTrace();
                }

        }

        return null;


    }
    @Override
    protected void onProgressUpdate (Void...values)
    {
        super.onProgressUpdate(values);

    }
    @Override
    protected void onPostExecute (String json)
    {
        JSONObject jsonObject= null;
        try
        {
            progressDialog.dismiss();
            jsonObject = new JSONObject(json);
            JSONArray jsonArray= jsonObject.getJSONArray("server_response");
            JSONObject JO = jsonArray.getJSONObject(0);
            String code= JO.getString("code");
            String message= JO.getString("message");
            if(code.equals("reg_true"))
            {
                showDialog("Regristration Success",message,code);
            }
            else if(code.equals("reg_false"))
            {
                showDialog("Regristration Failed",message,code);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
    public void showDialog(String title, String message, String code)
    {
        builder.setTitle(title);
        if(code.equals("reg_true")||code.equals("reg_false"))
        {
           builder.setMessage(message);
           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
                   activity.finish();
               }
           });
           AlertDialog alertDialog= builder.create();
           alertDialog.show();
        }
    }
}