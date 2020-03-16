package com.example.attendclasstpad.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.Toast;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.aty.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 服务器请求工具类
 *
 * @author chenhui 2019.01.03
 */
public class ServerRequestUtils {
    /**
     * 8000毫秒
     */
    public static final int REQUEST_LONG_TIME = 8000;
    /**
     * 1000毫秒
     */
    public static final int REQUEST_SHORT_TIME = 1000;

    /**
     * Post请求
     */
    public static final String VISIT_TYPE_FOR_POST = "POST";

    /**
     * Get请求
     */
    public static final String VISIT_TYPE_FOR_GET = "GET";

    private int requestTime;// 请求时间

    private Context context;
//    private ViewUtils vUtils;

    public interface OnServerRequestListener {
        public void onFailure(String msg);

        public void onResponse(String msg, JSONObject data, String count);
    }

    public interface OnServerRequestListener2 {
        public void onFailure(String msg);

        public void onResponse(String msg, JSONArray data, String count);
    }

    public ServerRequestUtils(Context context) {
        this.context = context;

//        vUtils = new ViewUtils(context);
    }

    /**
     * 同页面需多次请求,使用多个listener的情况。传参方式：jsonStr
     *
     * @param methodName 方法名
     * @param jsonStr    JSON字符串
     * @param dialogTip  加载框文字提示
     * @param listener   监听
     */
    public void request(String methodName, String jsonStr, String dialogTip,
                        OnServerRequestListener listener) {
        request(methodName, jsonStr, dialogTip, REQUEST_SHORT_TIME, listener);
    }

    /**
     * 同页面需多次请求,使用多个listener的情况，可自定义请求时间。传参方式：jsonStr
     *
     * @param methodName  方法名
     * @param jsonStr     JSON字符串
     * @param dialogTip   加载框文字提示
     * @param requestTime 请求时间
     * @param listener    监听
     */
    public void request(String methodName, String jsonStr, String dialogTip,
                        int requestTime, OnServerRequestListener listener) {
        // 时间赋值
        RequestBody formBody = FormBody.create(
                MediaType.parse("application/json; charset=utf-8"), jsonStr);

        request(methodName, formBody, dialogTip, requestTime, listener);
    }

    /**
     * 同页面需多次请求,使用多个listener2的情况。传参方式：jsonStr
     *
     * @param methodName 方法名
     * @param jsonStr    JSON字符串
     * @param dialogTip  加载框文字提示
     * @param listener2  监听
     */
    public void request(String methodName, String jsonStr, String dialogTip,
                        int requestTime, OnServerRequestListener2 listener2) {
        // 默认时间
        // this.requestTime = REQUEST_SHORT_TIME;

        RequestBody formBody = FormBody.create(
                MediaType.parse("application/json; charset=utf-8"), jsonStr);

        request(methodName, formBody, dialogTip, requestTime, listener2);
    }

    /**
     * 同页面需多次请求,使用多个listener的情况。传参方式：jsonStr
     *
     * @param methodName 方法名
     * @param formBody   JSON字符串
     * @param dialogTip  加载框文字提示
     * @param listener   监听
     */
    public void request(String methodName, RequestBody formBody,
                        String dialogTip, OnServerRequestListener listener) {
        request(methodName, formBody, dialogTip, REQUEST_SHORT_TIME, listener);
    }

    /**
     * 同页面需多次请求,使用多个listener的情况，可自定义请求时间。传参方式：jsonStr
     *
     * @param methodName  方法名
     * @param formBody    参数
     * @param dialogTip   加载框文字提示
     * @param requestTime 请求时间
     * @param listener    监听
     */
    public void request(String methodName, RequestBody formBody,
                        String dialogTip, int requestTime, OnServerRequestListener listener) {
        // 时间赋值
        this.requestTime = requestTime;

        request(methodName, formBody, dialogTip, listener, null);
    }

    /**
     * 同页面需多次请求,使用多个listener2的情况，可自定义请求时间。传参方式：jsonStr
     *
     * @param methodName  方法名
     * @param formBody    参数
     * @param dialogTip   加载框文字提示
     * @param requestTime 请求时间
     * @param listener2   监听
     */
    public void request(String methodName, RequestBody formBody,
                        String dialogTip, int requestTime,
                        OnServerRequestListener2 listener2) {
        // 时间赋值
        this.requestTime = requestTime;

        request(methodName, formBody, dialogTip, null, listener2);
    }

    /**
     * 服务器请求
     *
     * @param methodName 方法名
     * @param formBody   参数
     * @param dialogTip  加载框文字提示
     */
    public void request(String methodName, RequestBody formBody,
                        String dialogTip, final OnServerRequestListener listener,
                        final OnServerRequestListener2 listener2) {

        // 检测网络是否连接
        if (!NetworkUtils.checkNetworkState(context)) {
            Toast.makeText(context,
                    R.string.check_network_connections, Toast.LENGTH_SHORT)
                    .show();
        } else {
            // 获取头信息：token值
            String token = PreferencesUtils.acquireInfoFromPreferences(context,
                    ConstantsForPreferencesUtils.TOKEN);

            OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(requestTime, TimeUnit.MILLISECONDS)
                    .readTimeout(requestTime, TimeUnit.MILLISECONDS).build();
            Request request = new Request.Builder()
                    .url(UrlUtils.PREFIX_MOBILE + methodName)
                    .addHeader(ConstantsForServerUtils.AUTH_TOKEN, token)
                    .post(formBody).build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException ex) {
                    Handler uiHandler = new Handler(context.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 关闭加载框
                            // vUtils.dismissDialog();

                            if (listener != null) {
                                listener.onFailure("请求服务器失败，原因：" + ex.toString());
                            } else if (listener2 != null) {
                                listener2.onFailure("请求服务器失败，原因：" + ex.toString());
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {
                    String result = response.body().string();

                    response(result, listener, listener2);
                }
            });
        }
    }

    /**
     * 处理返回的数据
     *
     * @param result
     * @param listener
     */
    private void response(String result, OnServerRequestListener listener,
                          OnServerRequestListener2 listener2) {
        // JSONObject格式数据
        JSONObject resultJSONObj = ServerDataAnalyzeUtils.getJSONObject(result);

        // 获取文字消息
        String msg = ServerDataAnalyzeUtils.getValue(resultJSONObj,
                ConstantsForServerUtils.MSG);

        // 获取数据总条数
        String count = ServerDataAnalyzeUtils.getValue(resultJSONObj,
                ConstantsForServerUtils.COUNT);

        if (ServerDataAnalyzeUtils.isSuccessful(resultJSONObj)) {// 数据返回成功，将数据等信息由调用者自行处理
            if (listener != null) {
                // 获取到data数据
                JSONObject dataJSONObj = ServerDataAnalyzeUtils
                        .getDataAsJSONObject(resultJSONObj,
                                ConstantsForServerUtils.DATA);

                listener.onResponse(msg, dataJSONObj, count);
            } else if (listener2 != null) {
                // 获取到data数据
                JSONArray dataJSONArr = ServerDataAnalyzeUtils
                        .getDataAsJSONArray(resultJSONObj,
                                ConstantsForServerUtils.DATA);

                listener2.onResponse(msg, dataJSONArr, count);
            }
        } else {// 数据返回不成功，仅将显示信息由调用者自行处理
            if (listener != null) {
                listener.onFailure(msg);
            } else if (listener2 != null) {
                listener2.onFailure(msg);
            }
        }
    }

    /* 上传文件至Server的方法 */

    /**
     * 上传文件
     *
     * @param postUrl   接口地址
     * @param visitType 访问方式，如：POST
     * @param fileName  文件名，不能为空，如：QQ.png
     * @param filePath  文件完整路径,如：Tencent/QQFolder/QQ.png
     * @param dialogTip 弹框文字提示
     * @param listener  回调
     */
    @SuppressLint("NewApi")
    public void uploadFile(String postUrl, String visitType, String fileName,
                           String filePath, String dialogTip, OnServerRequestListener listener) {
        // 显示加载框
        // vUtils.showLoadingDialog(dialogTip);

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(postUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /*
             * Output to the connection. Default is false, set to true because
             * post method must write something to the connection
             */
            con.setDoOutput(true);
            /* Read from the connection. Default is true. */
            con.setDoInput(true);
            /* Post cannot use caches */
            con.setUseCaches(false);
            /* Set the post method. Default is GET */
            con.setRequestMethod(visitType);
            /* 设置请求属性 */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            // 获取头信息：token值
            String token = PreferencesUtils.acquireInfoFromPreferences(context,
                    ConstantsForPreferencesUtils.TOKEN);
            con.setRequestProperty(ConstantsForServerUtils.AUTH_TOKEN, token);

            /* 设置StrictMode 否则HTTPURLConnection连接失败，因为这是在主进程中进行网络连接 */
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());

            /* 设置DataOutputStream，getOutputStream中默认调用connect() */
            // output to the connection
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            // 符号：/" 即  "
            // "name=\"file\";filename=\" 即 name="file";filename="
            String str = "Content-Disposition: form-data; "
                    + "name=\"file\";filename=\"" + fileName + "\"" + end;

            ds.writeBytes(str);
            ds.writeBytes(end);
            /* 取得文件的FileInputStream */
            // 与根据File类对象的所代表的实际文件建立链接创建fileInputStream对象,filePath:文件完整路径
            FileInputStream fStream = new FileInputStream(filePath);
            /* 设置每次写入8192bytes */
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize]; // 8k
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* 关闭流，写入的东西自动生成Http正文 */
            fStream.close();
            /* 关闭DataOutputStream */
            ds.close();

            /* 正式建立HTTP连接,从返回的输入流读取响应信息 */
            BufferedReader is = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"));
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 显示网页响应内容 */
            // Toast.makeText(MainActivity.this, b.toString().trim(),
            // Toast.LENGTH_SHORT).show();//Post成功
            System.out.println(b.toString());

            String result = b.toString();
            response(result, listener, null);
        } catch (Exception e) {
            /* 显示异常信息 */
            // Toast.makeText(MainActivity.this, "Fail:" + e,
            // Toast.LENGTH_SHORT).show();//Post失败
            System.out.println(e);
        }
    }

    /**
     * @param path
     * @param property
     * @return
     * @throws Exception
     */
    @SuppressLint("NewApi")
    public byte[] getImage(String path, Map<String, String> property)
            throws Exception {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 基于HTTP协议的连接对象
        conn.setConnectTimeout(10000);// 请求超时时间 5s
        /*
         * Output to the connection. Default is false, set to true because post
         * method must write something to the connection
         */
        conn.setDoOutput(true);
        /* Read from the connection. Default is true. */
        conn.setDoInput(true);
        /* Post cannot use caches */
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");// 请求方式

        /* 设置请求属性 */
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        // conn.setRequestProperty("Content-Type",
        // "multipart/form-data;boundary=" + boundary);

        // 参数

        // 获取头信息：token值
        String token = PreferencesUtils.acquireInfoFromPreferences(context,
                ConstantsForPreferencesUtils.TOKEN);
        conn.setRequestProperty(ConstantsForServerUtils.AUTH_TOKEN, token);// EM
        // X0Hnv
        // ph0-e
        // MHIgf
        // nswkA
        // conn.setRequestProperty(ConstantsForServerUtils.AUTH_TOKEN,
        // "Gv 61VmI IX0q- THE56 -2rJw");

        if (property != null) {
            Iterator<String> keySet = property.keySet().iterator();
            while (keySet.hasNext()) {
                String key = keySet.next();
                String value = property.get(key);
                if (!ValidateFormatUtils.isEmpty(key)
                        && !ValidateFormatUtils.isEmpty(value)) {
                    conn.addRequestProperty(key, value);
                }
            }
        }

        // conn.connect();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        /* 正式建立HTTP连接,从返回的输入流读取响应信息 */
        BufferedReader is = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));
        int ch;
        StringBuffer b = new StringBuffer();
        while ((ch = is.read()) != -1) {
            b.append((char) ch);
        }
        /* 显示网页响应内容 */
        // Toast.makeText(MainActivity.this, b.toString().trim(),
        // Toast.LENGTH_SHORT).show();//Post成功
        System.out.println(b.toString());

        if (conn.getResponseCode() == 200) {// 响应码==200 请求成功
            InputStream inputStream = conn.getInputStream();// 得到输入流

            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                arrayOutputStream.write(buffer, 0, len);
            }
            inputStream.close();
            arrayOutputStream.close();
        }

        return null;
    }
}
