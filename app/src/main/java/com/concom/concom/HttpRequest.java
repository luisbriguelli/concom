package com.concom.concom;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Luis Fernando Briguelli da Silva on 27/11/2014.
 */
public class HttpRequest {
    private static final String AUTHORIZATION = "Authorization";
    private OkHttpClient client;
    private MediaType JSON;

    private static HttpRequest instance;

    private HttpRequest() {
        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");
    }

    public static HttpRequest getInstance() {
        if (instance == null)
            instance = new HttpRequest();
        return instance;
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        if(code >= 200 && code < 300){
            return response.body().string();
        }else{
            return null;
        }
    }

    public String get(String url, String authorization) throws IOException {
        Request request = new Request.Builder()
                .addHeader(AUTHORIZATION, authorization)
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        if(code >= 200 && code < 300){
            return response.body().string();
        }else{
            return null;
        }
    }

    //POST WITHOUT AUTHORIZATION (create account and login)
    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        if(code >= 200 && code < 300){
            return response.body().string();
        }else if(code == 422){
            return "";
        }else{
            return null;
        }
    }

    public String post(String url, String authorization, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader(AUTHORIZATION, authorization)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        if(code >= 200 && code < 300){
            return response.body().string();
        }else{
            return null;
        }
    }

    /*public int postLogout(String url, String authorization, String value) throws IOException {
        RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .addHeader(AUTHORIZATION, authorization)
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.code();
    }*/
}