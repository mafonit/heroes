package com.od.danich.heroes.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.od.danich.heroes.R;
import com.od.danich.heroes.view.ItemHero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class HttpRequest {
    private static final String LOG_TAG = "Attract/HttpRequest";
    private static final String CHARSET = "UTF-8";
    private static final String HEADER="application/json";
    private static final String ATTRACT_URL = "http://others.php-cd.attractgroup.com/test.json";
    private static final String GET = "GET";
    private StringBuilder result;
    private HttpURLConnection httpConnection;
    private HashMap<String,String> paramMap;
    private HttpResponseListener httpResponseListener;
    private HttpAsyncTask httpAsyncTask=null;
    private URL urlObj;
    private JSONArray jObj;
    private Context context;


    public HttpRequest(Context context){
        this.context=context;
    }


    public void setResponseListener(HttpResponseListener httpResponseListener){
        this.httpResponseListener=httpResponseListener;
    }


    public List getHeroList() {

        JSONArray jsonArrayResult = buildHttpRequest(GET, ATTRACT_URL, null);
        if(jsonArrayResult!=null) {
            List<ItemHero> itemArrayList = new ArrayList();
            try {
                for (int i = 0; i < jsonArrayResult.length(); i++) {
                    JSONObject jsonObject = jsonArrayResult.getJSONObject(i);

                    ItemHero item = new ItemHero();
                    item.setItemId(jsonObject.optString("itemId"));
                    item.setName(jsonObject.optString("name"));
                    item.setImage(jsonObject.optString("image"));
                    item.setDescription(jsonObject.optString("description"));
                    item.setTime(jsonObject.optLong("time"));
                    itemArrayList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return itemArrayList;
        }else return null;
    }




    public JSONArray buildHttpRequest(String typeRequest, String uri, HashMap<String, String> paramMap) {
        if(!isOnline(context)){
            httpResponseListener.handleError(context.getResources().getString(R.string.network_disable));return null;}
        if(httpAsyncTask!=null){
            httpAsyncTask.cancel(true);
        }
        RequestParams paramsObj = new RequestParams();
        paramsObj.setTypeRequest(typeRequest);
        paramsObj.setUrl(uri);

        if(paramMap!=null) {
            paramsObj.setParams(paramMap);
        }

        JSONArray jsonObj= null;
        try {
            httpAsyncTask = new HttpAsyncTask();
            jsonObj = httpAsyncTask.execute(paramsObj).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return jsonObj;
    }


    public class HttpAsyncTask extends AsyncTask<RequestParams, String, JSONArray> {
        @Override
        public void onPreExecute() {
        }

        @Override
        public JSONArray doInBackground(RequestParams... params) {
            JSONArray json = makeConnection(params[0]);
            return json;
        }

        public void onPostExecute(JSONArray json) {
        }
    }




    public JSONArray makeConnection(RequestParams params) {
        String typeRequest = params.getTypeRequest();
        String url = params.getUrl();
        paramMap = params.getParams();

        try {
            urlObj = new URL(url);
            httpConnection = (HttpURLConnection) urlObj.openConnection();
            httpConnection.setRequestMethod(typeRequest);
            httpConnection.setRequestProperty("Accept-Charset", CHARSET);
            httpConnection.setRequestProperty("Accept", HEADER);

            httpConnection.setReadTimeout(10000);
            httpConnection.setConnectTimeout(15000);
            httpConnection.connect();

            int  responseCode=httpConnection.getResponseCode();
            if(responseCode!=200){
                httpConnection.disconnect();
                httpResponseListener.handleError("Произошла ошибка " + responseCode + " , обратитесь в техподдержку");
                return null;
            }

        } catch (MalformedURLException e) {
            httpResponseListener.handleError("MalformedURLException");
            e.printStackTrace();
        } catch (ProtocolException e) {
            httpResponseListener.handleError("ProtocolException");
            e.printStackTrace();
        } catch (ConnectException e) {
            httpResponseListener.handleError("ConnectException");

            e.printStackTrace();
            return null;
        } catch (RuntimeException e) {
            httpResponseListener.handleError("RuntimeException");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            httpResponseListener.handleError("IOException");
        }


        try {
            InputStream in = new BufferedInputStream(httpConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                Log.d(LOG_TAG, "result: " + result.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jObj = new JSONArray(result.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing data " + e.toString());
        }

        httpConnection.disconnect();

        return jObj;
    }




    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}





