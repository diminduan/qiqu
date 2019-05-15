package com.example.duand.qiqu.Utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetHttpConnection extends Thread {

    private String url;
    private Handler handler;

    public GetHttpConnection(String url,Handler handler){
        this.url = url;
        this.handler = handler;
    }

    public GetHttpConnection(String url){
        this.url = url;
    }

    @Override
    public void run(){
        HttpURLConnection connection = null;
        try {
            URL httpUrl = new URL(url);
            connection = (HttpURLConnection)httpUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null){
                response.append(line);
            }

            Message msg = new Message();
            msg.what = 1;
            msg.obj = response.toString();
            handler.sendMessage(msg);

            in.close();
            reader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }

    }
}
