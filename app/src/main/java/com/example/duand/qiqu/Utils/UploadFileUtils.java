package com.example.duand.qiqu.Utils;


import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther:richard
 * @Date:2019/5/20
 * @Description:${package}
 * @Version:1.0
 */


public class UploadFileUtils {
    /**
     * 上传单文件，文件名，上传路径匹配服务端的uploadPic以及uploadBoutiqueRoutePic，注意参数的编写。
     * @param fileName  文件名
     * @param url
     */
    public static void uploadFile(String fileName, String url){
        CloseableHttpClient httpClient=null;
        CloseableHttpResponse response=null;
        try{
            httpClient= HttpClients.createDefault();
            HttpPost httpPost=new HttpPost(url);
            FileBody bin=new FileBody(new File(fileName));
            StringBody fileKey=new StringBody("uploadFile", ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8));
            HttpEntity reqEntity= MultipartEntityBuilder.create().addPart("uploadFile",bin).addPart("fileKey",fileKey).build();
            httpPost.setEntity(reqEntity);
            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);
            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 打印响应长度
                System.out.println("Response content length: " + resEntity.getContentLength());
                // 打印响应内容
                System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
            }
            // 销毁
            EntityUtils.consume(resEntity);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 多文件上传用于上传动态的图片
     * @param fileNames  图片的路径
     * @param url    上传的路径注意参数格式，字符串拼接
     */
    public static void uploadFiles(String[] fileNames, String url){
        CloseableHttpClient httpClient=null;
        CloseableHttpResponse response=null;
        try{
            httpClient= HttpClients.createDefault();
            HttpPost httpPost=new HttpPost(url);
            List<FileBody> bins=new ArrayList<FileBody>();
            for(String fileName:fileNames){
                bins.add(new FileBody(new File(fileName)));
            }
            StringBody fileKey=new StringBody("uploadFile", ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8));

            HttpEntity reqEntity=null;
            MultipartEntityBuilder multipartEntityBuilder =MultipartEntityBuilder.create();
            for(FileBody fileBody:bins){
                multipartEntityBuilder.addPart("multipartFiles",fileBody);
            }

            reqEntity= multipartEntityBuilder.addPart("fileKey",fileKey).build();

            httpPost.setEntity(reqEntity);
            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);
            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 打印响应长度
                System.out.println("Response content length: " + resEntity.getContentLength());
                // 打印响应内容
                System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
            }
            // 销毁
            EntityUtils.consume(resEntity);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
