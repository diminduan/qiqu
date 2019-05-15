package com.example.duand.qiqu.Utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.duand.qiqu.Activity.NewRouteActivity;

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
import java.net.ProtocolException;
import java.net.URL;

public class HttpPost extends Thread {
    private String url;
    private Handler handler;
    private JSONObject json;

    public HttpPost(String url, Handler handler, JSONObject json){
        this.url = url;
        this.handler = handler;
        this.json = json;
    }

    @Override
    public void run(){
        HttpURLConnection connection = null;

        try {
            URL httpurl = new URL(url);
            connection = (HttpURLConnection) httpurl.openConnection();   //打开http连接
            connection.setConnectTimeout(5000);   //连接的超时时间
            connection.setReadTimeout(5000);   //响应的超时时间
            connection.setUseCaches(false);   //不使用缓存
            connection.setDoInput(true);    //设置这个连接是否可以写入数据
            connection.setDoOutput(true);   //设置这个连接是否可以输出数据
            connection.setRequestMethod("POST");   //设置请求的方式
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8"); //设置消息的类型
            connection.connect();
            String jsonStr = json.toString();   //把JSON对象按JSON的编码格式转换为字符串
            OutputStream out = connection.getOutputStream();    //输出流，用来发送请求
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));   //创建字符流对象并用高效缓冲流包装
            bw.write(jsonStr);   //把json字符串写入缓冲区中
            bw.flush();   //刷新缓冲区，把数据发送出去
            out.close();
            bw.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine())!= null){
                    response.append(line);
                }

                in.close();
                reader.close();

                Message msg = new Message();
                msg.what = 1;
                msg.obj = response.toString();
                handler.sendMessage(msg);

            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
}
