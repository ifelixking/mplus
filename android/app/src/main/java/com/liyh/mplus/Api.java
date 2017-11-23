package com.liyh.mplus;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

import java.io.UnsupportedEncodingException;
import com.liyh.mplus.data.Result;

/**
 * Created by liyh on 2017/11/10.
 */

class Api {
	
	private static String m_token = "";        // JSESSIONID
	
	private static void httpGet(final String url, final Handler handler) {
		HttpGet httpGet = new HttpGet(url);
		httpRequest(httpGet, handler);
	}
	
	private static void httpPost(final String url, @Nullable final String postData, final Handler handler) {
		HttpPost httpPost = new HttpPost(url);
		if (postData != null) {
			try {
				httpPost.setEntity(new StringEntity(postData, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(3, "参数编码不支持"));
			}
		}
		httpRequest(httpPost, handler);
	}
	
	private static String prefix() {
		return "http://" + Application.getContext().getResources().getString(R.string.server_address);
	}
	
	private static void httpRequest(final HttpRequestBase request, final Handler handler) {
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
						handler.sendMessage(handler.obtainMessage(0, responseText));
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
		JsonObject params = new JsonObject();
		params.addProperty("mobile", mobile);
		params.addProperty("device", deviceID);
		params.addProperty("password", password);
		httpPost(url, params.toString(), new Handler(Application.getContext().getMainLooper()) {
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					Result result = Utils.Json.deserialize((String)msg.obj, Result.class);
					if (result.isSuccess()) {
						m_token = (String) result.detail;
					}
				}
				// TODO: 改造
				Message msg2 = new Message();
				msg2.what = msg.what;
				msg2.obj = msg.obj;
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
		httpPost(url, null, handler);
	}
	
	static void getFriends(Handler handler) {
		String url = prefix() + "/users/friends";
		httpGet(url, handler);
	}

    static void getMessage(long friendID, Handler handler) {
        String url = prefix() + "/msg/" + friendID;
        httpGet(url, handler);
    }
}
