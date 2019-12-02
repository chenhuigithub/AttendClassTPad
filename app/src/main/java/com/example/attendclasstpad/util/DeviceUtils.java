package com.example.attendclasstpad.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

/**
 * 设备工具类
 *
 * @author chenhui
 */
@SuppressWarnings("deprecation")
public class DeviceUtils {
    private static final String TODO = "";
    private Context context;
    private TelephonyManager tm;

    public DeviceUtils(Context context) {
        this.context = context;
        tm = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
    }

    /**
     * 设备型号
     *
     * @return
     */
    public String getModel() {
        String num = android.os.Build.MODEL;
        if (ValidateFormatUtils.isEmpty(num)) {
            return "";
        }
        return num;
    }

    /**
     * 设备名
     *
     * @return
     */
    public String getName() {
        String name = android.os.Build.DEVICE;
        if (ValidateFormatUtils.isEmpty(name)) {
            return "";
        }
        return name;
    }

    /**
     * 设备厂商名
     *
     * @return
     */
    public String getManuFacturer() {
        String name = android.os.Build.MANUFACTURER;
        if (ValidateFormatUtils.isEmpty(name)) {
            return "";
        }
        return name;
    }

    /**
     * 设备硬件序列号
     *
     * @return
     */
    @SuppressLint("NewApi")
    public String getSerialNum() {
        String num = android.os.Build.SERIAL;
        if (ValidateFormatUtils.isEmpty(num)) {
            return "";
        }
        return num;
    }

    /**
     * 设备Android版本
     *
     * @return
     */
    public String getAndroidVersion() {
        String code = android.os.Build.VERSION.RELEASE;
        if (ValidateFormatUtils.isEmpty(code)) {
            return "";
        }
        return code;
    }

    /**
     * 设备IMEI,设备唯一标识
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public String getIMEI() {
        String imei = "";
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return TODO;
            }
            imei = tm.getDeviceId();
        }

        if (ValidateFormatUtils.isEmpty(imei)) {
            return "UnKnown";
        }

        return imei;
    }
}
