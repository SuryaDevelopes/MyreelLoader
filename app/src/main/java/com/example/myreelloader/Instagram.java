package com.example.myreelloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Instagram {
    public static void download(Context context, String url) {
        RequestQueue requestQueue;
        String[] ids=url.split("/");
        String jsurl="https://www.instagram.com/p/"+ids[4]+"/?__a=1";
        final String[] finalVideoUrl = new String[1];
        requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, jsurl, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = response.getJSONObject("graphql");
                        Log.d("JSONOBJ", "Passes Obj1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject2 = null;
                    try {
                        assert jsonObject1 != null;
                        jsonObject2 = jsonObject1.getJSONObject("shortcode_media");
                        Log.d("JSONOBJ", "Passes Obj2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert jsonObject2 != null;
                        finalVideoUrl[0]= jsonObject2.getString("video_url");
                        Log.d("JSONOBJ", "Passes url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Final Video URl", finalVideoUrl[0]);
                    Util.download(finalVideoUrl[0], Util.RootDirectoryInstagram, context, System.currentTimeMillis() + ".mp4");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VideoURLErrors", "Something went wrong" + error);
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
}

class Util {
    public static String RootDirectoryInstagram = "/Insta Video Downloader/instagram videos/";

    public static void download(String downloadPath, String destinationPath, Context context, String fileName) {
        Toast.makeText(context, "Downloading started...", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(downloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI );
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, destinationPath + fileName);
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }
}
