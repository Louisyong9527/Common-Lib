package com.common.lib.common.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by FPM0300 on 2018/3/23.
 */

public class UpdateUtils {


    /**
     * 安装apk
     */
    public static void installApk(File file, Activity mActivity) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mActivity, "com.fosconn.fileprovider" , file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mActivity.startActivity(intent);
    }

    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    public static File getFileFromServer(String uri, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(uri);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            long time= System.currentTimeMillis();//当前时间的毫秒数
            //File file = new File(Environment.getExternalStorageDirectory(), time+"updata.apk");

            File file = new File(Environment.getExternalStorageDirectory(), time+"updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }

    /*
         * 获取当前程序的版本名
         */
    public static String getVersionName(Activity mContext ) throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
//        Log.e("TAG", "版本号" + packInfo.versionCode);
//        Log.e("TAG", "版本名" + packInfo.versionName);
        return packInfo.versionName;

    }

    /**
     * 版本号比较
     *
     * @param version1 接口版本
     * @param version2 app自身版本
     * @return
     */
    public static Boolean compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return false; //版本相同，无需升级
        }

        if (TextUtils.isEmpty(version1)|| TextUtils.isEmpty(version2)){
            return false; //获取版本异常，无需升级
        }


        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");

        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小

        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return true; //接口版本较大，需要升级
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return false;//当前版本较大，无需升级
                }
            }
            return false; //版本相同，无需升级
        } else {
            return diff > 0;
        }
    }

}
