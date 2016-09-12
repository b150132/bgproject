package com.lytech.rainsilk.webviewapp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by rainsilk on 2016/2/17.
 */

public class DBConnector {
    public static final int CONNECTION_TIMEOUT = 15000; //15 second
    public static final String SERVER_ADDRESS = "http://lytechly.comxa.com/";

    //查詢空車 (計程車,機車)
    public static String executeQuery(String query_string) {
        String result = "";

        try {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("service", query_string));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchServiceData.php");

            post.setEntity(new UrlEncodedFormEntity(data_to_send));
            HttpResponse httpResponse = client.execute(post);
            HttpEntity entity = httpResponse.getEntity();

            InputStream inputStream = entity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        } catch (Exception e) {

        }

        return result;
    }

    //查詢點數用完客人 DisplayCountOutGuest
    public static String executeCountOutQuery(String page,String column,String value) {
        String result = "";

        try {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("page", page));
            data_to_send.add(new BasicNameValuePair("column", column));
            data_to_send.add(new BasicNameValuePair("value", value));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchAnything.php");

            post.setEntity(new UrlEncodedFormEntity(data_to_send));
            HttpResponse httpResponse = client.execute(post);
            HttpEntity entity = httpResponse.getEntity();

            InputStream inputStream = entity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();

        } catch (Exception e) {

        }

        return result;
    }

    //查詢商品名稱及價格
    public static String executeQueryComm(String query_shop) {
        String result = "";

        try {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("shop", query_shop));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchCommName.php");

            post.setEntity(new UrlEncodedFormEntity(data_to_send));
            HttpResponse httpResponse = client.execute(post);
            HttpEntity entity = httpResponse.getEntity();

            InputStream inputStream = entity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        } catch (Exception e) {

        }

        return result;
    }

    //查詢跑馬燈文字
    public static String executeQueryMar(String query_area) {
        String result = "";

        try {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("area", query_area));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchMar.php");

            post.setEntity(new UrlEncodedFormEntity(data_to_send));
            HttpResponse httpResponse = client.execute(post);
            HttpEntity entity = httpResponse.getEntity();

            InputStream inputStream = entity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        } catch (Exception e) {

        }

        return result;
    }

    //查詢商店
    public static String executeQueryShop(String query_area) {
        String result = "";

        try {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("area", query_area));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchShop.php");

            post.setEntity(new UrlEncodedFormEntity(data_to_send));
            HttpResponse httpResponse = client.execute(post);
            HttpEntity entity = httpResponse.getEntity();

            InputStream inputStream = entity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        } catch (Exception e) {

        }

        return result;
    }

    //查詢客人
    public static String executeQueryGuest(String query_area) {
        String result = "";

        try {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("area", query_area));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchGuest.php");

            post.setEntity(new UrlEncodedFormEntity(data_to_send));
            HttpResponse httpResponse = client.execute(post);
            HttpEntity entity = httpResponse.getEntity();

            InputStream inputStream = entity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        } catch (Exception e) {

        }

        return result;
    }
}
