package com.example.attendclasstpad.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 图片格式转化工具类
 *
 * @author zhaochenhui, 2016.07.23
 */
public class PicFormatUtils {
    public final static int SIGN_FOR_BITMAP = 1001;

    /**
     * 把Bitmap转换成byte[ ]
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        // 实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, baos);// 压缩位图
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();// 创建分配字节数组
    }

    /**
     * 把byte[ ]转换回来Bitmap
     *
     * @param data
     * @return
     */

    public static Bitmap getBitmap(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);// 从字节数组解码位图
    }

    /**
     * 取出Drawable中的Bitmap
     *
     * @param drawable
     * @return
     */
    public Bitmap getBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    /**
     * 把Bitmap转为Drawable
     *
     * @param bm
     * @return
     */
    @SuppressWarnings("deprecation")
    public Drawable getDrawable(Bitmap bm) {
        return new BitmapDrawable(bm);
    }


    public Drawable getDrawable(String base64) {
        ByteArrayInputStream bis = new ByteArrayInputStream(android.util.Base64.encode(base64.getBytes(), android.util.Base64.DEFAULT));
        Drawable drawable = Drawable.createFromStream(bis, "");
        return drawable;
    }

    /**
     * 根据图片url获取图片对象:Bitmap格式
     *
     * @param urlpath
     * @return Bitmap 根据图片url获取图片对象
     */
    public void getBitmap(final String urlpath, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bm = null;
                    URL url = new URL(urlpath);
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    InputStream in;
                    in = conn.getInputStream();

                    // byte[] imagebytes;
                    // try {
                    // imagebytes = getBytes(in);
                    // bm = BitmapFactory.decodeByteArray(imagebytes, 0,
                    // imagebytes.length);
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }

                    // PicUtils putils = new PicUtils();
                    // putils.comp(bm);

                    bm = BitmapFactory.decodeStream(in);

                    Message msg = handler.obtainMessage();
                    msg.what = SIGN_FOR_BITMAP;
                    msg.obj = bm;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        bos.flush();
        byte[] result = bos.toByteArray();
        System.out.println(new String(result));
        return result;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap, CompressFormat format) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(format, 80, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据图片url获取图片对象:Drawable格式，异步获取方式
     *
     * @param imageUrl
     * @return
     */
    @SuppressLint("LongLogTag")
    public Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "");
        } catch (IOException e) {
            Log.d("PicFormatUtils_loadImageFromNetwork", e.getMessage());
        }

        return drawable;
    }

    /**
     * Bimmap格式图片转为File格式图片
     *
     * @param context
     * @param bitmap
     * @return
     */
    private File convertBitmapToFile(Context context, Bitmap bitmap) {
        File f = null;
        try {
            // create a file to write bitmap data
            f = new File(context.getCacheDir(), "portrait");
            f.createNewFile();

            // convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */,
                    bos);
            byte[] bitmapdata = bos.toByteArray();

            // write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
        }
        return f;
    }
}
