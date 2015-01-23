package com.huoqiu.framework.rest;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.encrypt.Encrypt;
import com.huoqiu.framework.util.DateUtil;
import com.huoqiu.framework.util.DeviceUtil;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AppAuthInterceptor implements ClientHttpRequestInterceptor {
    private final static String TAG = "ManyiJSON";
    /**
     * 报文头需要的字段
     */
    private String appTime = "App_Time";// 当前时间
    private String appVersion = "ver"; // 手机端版本号
    private String appOS = "os"; // 手机来源 (android/ios)
    private String appIMEI = "imei"; // 手机唯一标志
    private String appModel = "model"; // 手机型号
    private String userId = "user_id"; // 已登录用户ID
    private String u_ticket = "u_ticket";// 已登录用户的Cookie

    private String appKeyLabel = "App-Key"; // key
    private String appSecretLabel = "App-Secret"; // secret
    private String distance = "distance";


    @SuppressWarnings("unchecked")
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        request.getHeaders().setAcceptEncoding(Arrays.asList(ContentCodingType.GZIP, ContentCodingType.ALL));
        Map<String, Object> map;
        if (data.length > 0) {
            map = objectMapper.readValue(data, Map.class);
        } else {
            data = "".getBytes();
            map = new HashMap<String, Object>(0);
        }

        if (Configuration.DEFAULT == Configuration.TEST || Configuration.DEFAULT == Configuration.IWJW_TEST) {
            // 非正式版本，打印服务Log
            showRequestJSON(map);
        }

        List<Map.Entry<String, Object>> params = new ArrayList<Map.Entry<String, Object>>(map.entrySet());//
        Collections.sort(params, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
                if (o1.getKey() == null || o2.getKey() == null)
                    return 0;
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        String secret = "";
        for (Map.Entry<String, Object> param : params) {
            String key = param.getKey();
            Object value = param.getValue();
            secret = secret + key + "=" + value + "&";
        }
        if (secret.endsWith("&"))
            secret = secret.substring(0, secret.length() - 1);

        String timestamp = String.valueOf((DateUtil.getCurrentTime().getTimeInMillis() / 100000));
        String md5 = Encrypt.decryptKey(secret, timestamp, BackOpFragmentActivity.PACKAGE_NAME);

        HttpHeaders headers = request.getHeaders();
        headers.add(appKeyLabel, Configuration.DEFAULT.appKey);
        headers.add(appSecretLabel, md5);
        headers.add(appTime, DateUtil.getCurrentTime().getTimeInMillis() + "");
        headers.add(appVersion, AppConfig.versionName);
        headers.add(appOS, "android");
        headers.add(appIMEI, DeviceUtil.getIMEI(AppConfig.application));
        headers.add(appModel, DeviceUtil.getBrandModel());
        headers.add(userId, AppConfig.UID);
        headers.add(u_ticket, AppConfig.u_ticket);
        headers.add(distance, AppConfig.distance);


        headers.set("Connection", "Closed");

        return execution.execute(request, data);
    }


    /**
     * 打印Log
     *
     * @param map
     */
    private void showRequestJSON(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        for (String string : map.keySet()) {
            builder.append(string);
            builder.append("=");
            builder.append(map.get(string));
            builder.append(";");
        }

        Log.i(TAG, "------------------- resquest Start --------------");
        Log.i(TAG, builder.toString());
        Log.i(TAG, "------------------- resquest  End  --------------");

    }

}