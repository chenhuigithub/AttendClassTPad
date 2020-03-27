package com.example.attendclasstpad.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.Iterator;

/**
 * 解析服务器返回数据格式相关方法
 *
 * @author zhaochenhui
 */
public class ServerDataAnalyzeUtils {
    /**
     * 获取到Object格式的数据
     *
     * @param jsonObj
     * @param key
     * @return
     * @author zhaochenhui, 2016.07.06
     */
    public static Object getDataAsObject(JSONObject jsonObj, String key) {
        if (jsonObj == null) {
            return null;
        }

        if (jsonObj.isNull(key)) {// 判断若是null的情况就返回null，防止程序崩溃（注意：此null非自己定义的null值，不可用寻常方法判断）
            return null;
        }

        Object dataObj = null;
        if (jsonObj.has(key)) {// 返回的数据中包含此字段
            try {
                dataObj = jsonObj.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return dataObj;
    }

    /**
     * 将SoapObject格式数据转为JSONObject格式返回
     *
     * @param soapObj
     * @return
     * @author zhaochenhui, 2016.06.08
     */
    public static JSONObject getJSONObjFromSoapObj(SoapObject soapObj) {
        JSONObject resultJsonObj = null;
        if (soapObj == null) {
            return null;
        }

        Object obj = soapObj.getProperty(0);
        try {
            resultJsonObj = new JSONObject(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return resultJsonObj;
    }

    /**
     * 判断服务器是否成功返回数据
     *
     * @param jsonObj 数据
     * @return
     */
    public static boolean isSuccessful(JSONObject jsonObj) {
        if (jsonObj == null) {
            return false;
        }

        if (jsonObj.isNull(ConstantsForServerUtils.CODE)) {// 判断若是null的情况就返回false，防止程序崩溃（注意：此null非自己定义的null值，不可用寻常方法判断）
            return false;
        }

        String code;

        if (jsonObj.has(ConstantsForServerUtils.CODE)) {// 返回的数据中包含此字段
            try {
                code = jsonObj.getString(ConstantsForServerUtils.CODE);// 获取返回的信息
                if (ConstantsForServerUtils.SUCCESS_VALUE.equals(code)) {// 返回数据成功
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 获取服务器返回的指定字段包含的String类型的数据
     *
     * @param jsonObj
     * @return 该字段包含的数据
     */
    public static String getValue(JSONObject jsonObj, String key) {
        if (jsonObj == null) {
            return "";
        }

        if (jsonObj.isNull(key)) {// 判断若是null的情况就返回""，防止程序崩溃（注意：此null非自己定义的null值，不可用寻常方法判断）
            return "";
        }

        String value = "";
        if (jsonObj.has(key)) {
            try {
                value = jsonObj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return value;
    }

    /**
     * 获取到JSONObject格式的数据
     *
     * @param jsonObj
     * @return
     */
    public static JSONObject getDataAsJSONObject(JSONObject jsonObj, String key) {
        if (jsonObj == null) {
            return null;
        }

        if (jsonObj.isNull(key)) {// 判断若是null的情况就返回null，防止程序崩溃（注意：此null非自己定义的null值，不可用寻常方法判断）
            return null;
        }

        JSONObject dataObj = null;
        if (jsonObj.has(key)) {// 返回的数据中包含此字段
            try {
                dataObj = jsonObj.getJSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return dataObj;
    }

    /**
     * 获取到JSONArray格式的数据
     *
     * @param jsonObj
     * @return
     */
    public static JSONArray getDataAsJSONArray(JSONObject jsonObj, String key) {
        if (jsonObj == null) {
            return null;
        }

        if (jsonObj.isNull(key)) {// 判断若是null的情况就返回null，防止程序崩溃（注意：此null非自己定义的null值，不可用寻常方法判断）
            return null;
        }

        JSONArray dataArr = null;
        if (jsonObj.has(key)) {// 返回的数据中包含此字段
            try {
                dataArr = jsonObj.getJSONArray(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return dataArr;
    }

    /**
     * 通过遍历JSONObject格式的数据，获取单条数据，并将整体转化为JSONArray格式数据
     *
     * @param dataObj 最里层JSONObject数据，例如：data:{"id":"id"}
     *                注意：返回数据格式为org.json.JSONArray
     */
    public static JSONArray getDataAsJSONArray(JSONObject dataObj) {
        if (dataObj == null) {
            return null;
        }

        JSONArray dataArr = new JSONArray();
        Iterator<String> keys = dataObj.keys();
        String keyItem;
        Object objItem;
        JSONObject dataObjItem = null;
        try {
            while (keys.hasNext()) {
                keyItem = keys.next();
                objItem = dataObj.get(keyItem);
                if (objItem instanceof JSONObject) {
                    dataObjItem = (JSONObject) objItem;
                    dataArr.put(dataObjItem);
                }
                if (objItem instanceof String) {
                    JSONObject dataObjItem1 = new JSONObject();
                    String valueStrItem = objItem.toString();
                    dataObjItem1.put(keyItem, valueStrItem);
                    dataArr.put(dataObjItem1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataArr;
    }

    /**
     * 获取JSONObject格式数据
     *
     * @param result 数据字符串
     * @return
     */
    public static JSONObject getJSONObject(String result) {
        if (ValidateFormatUtils.isEmpty(result)) {
            return null;
        }

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("服务器返回数据解析", "返回格式不是JSONObject");
        }

        return jsonObj;
    }
}
