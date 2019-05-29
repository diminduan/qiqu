package com.example.duand.qiqu.Utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class okhttpFileUpload {
    public static ResponseBody fileupload(String url, String filename, String path) throws IOException {
        OkHttpClient client = new OkHttpClient();  //创建client对象
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uploadFile", filename,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(path)))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body();
    }
}
