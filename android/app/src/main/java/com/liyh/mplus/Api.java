package com.liyh.mplus;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by liyh on 2017/11/10.
 */

class Api {

    private static String m_token = "";        // JSESSIONID

    private static void httpGet(final String url, final Handler handler) {
        HttpGet httpGet = new HttpGet(url);
        httpRequestJson(httpGet, handler);
    }

    private static void httpPostJson(final String url, @Nullable final JSONObject postData, final Handler handler) {
        HttpPost httpPost = new HttpPost(url);
        if (postData != null) {
            try {
                httpPost.setEntity(new StringEntity(postData.toString(), HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                handler.sendMessage(handler.obtainMessage(3, "参数编码不支持"));
            }
        }
        httpRequestJson(httpPost, handler);
    }

    private static String prefix() {
        return "http://" + Application.getContext().getResources().getString(R.string.server_address);
    }

    private static void httpRequestJson(final HttpRequestBase request, final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    Log.i("liyh - access-token", m_token);
                    request.addHeader("access-token", m_token);        // Cookie
                    request.addHeader("Content-Type", "application/json");
                    HttpResponse httpResponse = httpClient.execute(request);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity httpEntity = httpResponse.getEntity();
                        String responseText = EntityUtils.toString(httpEntity, "utf-8");
                        Log.i("liyh - responseText", responseText);
                        try {
                            JSONObject jsonObject = new JSONObject(responseText);
                            Log.i("liyh - responseJson", jsonObject.toString());
                            handler.sendMessage(handler.obtainMessage(0, jsonObject));
                        } catch (JSONException e) {
                            handler.sendMessage(handler.obtainMessage(3, "返回数据为非法Json格式"));
                        }
                    } else {
                        handler.sendMessage(handler.obtainMessage(1, "服务器异常"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendMessage(handler.obtainMessage(2, "网络异常"));
                }
            }
        }.start();
    }

    static void hasPwd(String mobile, Handler handler) {
        String url = prefix() + "/users/hasPwd/" + mobile;
        httpGet(url, handler);
    }

    static void login(String mobile, String deviceID, String password, final Handler handler) {
        String url = prefix() + "/users/login";
        JSONObject params = new JSONObject();
        try {
            params.put("mobile", mobile);
            params.put("device", deviceID);
            params.put("password", password);
        } catch (JSONException ignore) {
        }
        httpPostJson(url, params, new Handler(Application.getContext().getMainLooper()) {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    if (Utils.Json.isSuccessResult((JSONObject) msg.obj)) {
                        m_token = Utils.Json.jsString((JSONObject) msg.obj, "detail");
                    }
                }
                Message msg2 = new Message(); msg2.what = msg.what; msg2.obj = msg.obj;
                handler.sendMessage(msg2);
            }
        });
    }

    static void search(Handler handler) {
        String url = prefix() + "/users/search";
        httpGet(url, handler);
    }

    static void addFriend(long userID, Handler handler) {
        String url = prefix() + "/users/addFriend?userID=" + userID;
        httpPostJson(url, null, handler);
    }

    static void getFriends(Handler handler) {
        String url = prefix() + "/users/friends";
        httpGet(url, handler);
    }
}
