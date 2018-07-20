package com.common.lib.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.lib.common.AppManager;
import com.common.lib.common.BaseApplication;
import com.common.lib.common.R;
import com.common.lib.common.bean.ContactBen;
import com.common.lib.common.view.MyToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version V1.0
 * @description 辅助类
 */

@SuppressLint("NewApi")
public class AndroidUtils {
    private static final String TAG = "AndroidUtils";
    private static final String SP_NAME_RESTART = "spnr";
    /**
     * 未连接
     */
    public static final int NETWORK_INAVAILABLE = -100;
    public static final String SU_FILE = File.separator + "system" + File.separator + "bin" + File.separator + "su";
    private static SharedPreferences mShareConfig;
    private static SharedPreferences mShareConfigRestart;
    /**
     * uid默认字符串前缀
     **/
    private static final String DEFAULT_STRING = "0000";

    /**
     * 判断网络类型
     *
     * @return
     */
    public static boolean isWap() {
        final String proxyHost = android.net.Proxy.getDefaultHost();
        if (proxyHost != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否有SD卡
     *
     * @return
     */
    public static boolean isAvaiableSDCard() {

        final String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到本地IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (final Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                final NetworkInterface intf = en.nextElement();
                for (final Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    final InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (final SocketException ex) {
            Log.i(TAG, " AndroidUtils getLocalIpAddress : " + ex.toString());
        }
        return null;
    }

    /**
     * 根据key得到信息
     *
     * @param key
     */
    public static String getStringByKey(Context context, String key) {
        String value = null;
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                value = mShareConfig.getString(key, "");
            }
        }
        try {
            return SimpleCrypto.decrypt(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;

    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName
     *            ：应用包名
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 根据key得到信息
     *
     * @param key
     */
    public static int getIntByKey(Context context, String key) {
        int value = 0;
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                value = mShareConfig.getInt(key, 0);
            }
        }
        return value;
    }

    /**
     * 根据key得到boolean信息
     *
     * @param key
     */
    public static boolean getBooleanByKey(Context context, String key) {
        boolean value = false;
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                value = mShareConfig.getBoolean(key, false);
            }
        }
        return value;
    }

    /**
     * 根据key得到long信息
     *
     * @param key
     */
    public static long getLongByKey(Context context, String key) {
        long value = -1l;
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                value = mShareConfig.getLong(key, -1l);
            }
        }
        return value;
    }

    /**
     * 保存boolean值
     *
     * @param key
     * @param value
     */
    public static boolean saveBooleanByKey(Context context, String key, boolean value) {
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                final Editor edit = mShareConfig.edit();
                edit.putBoolean(key, value);
                edit.commit();
            }
        }
        return value;
    }

    /**
     * 保存boolean值
     *
     * @param key
     * @param value
     */
    public static boolean saveBooleanRestart(Context context, String key, boolean value) {
        if (!TextUtils.isEmpty(key)) {
            mShareConfigRestart = context.getSharedPreferences(SP_NAME_RESTART, Context.MODE_PRIVATE);
            if (null != mShareConfigRestart) {
                final Editor edit = mShareConfigRestart.edit();
                edit.putBoolean(key, value);
                edit.commit();
            }
        }
        return value;
    }

    /**
     * 根据key得到boolean信息
     *
     * @param key
     */
    public static boolean getBooleanByRestartKey(Context context, String key) {
        boolean value = false;
        if (!TextUtils.isEmpty(key)) {
            mShareConfigRestart = context.getSharedPreferences(SP_NAME_RESTART, Context.MODE_PRIVATE);
            if (null != mShareConfigRestart) {
                value = mShareConfigRestart.getBoolean(key, false);
            }
        }
        return value;
    }

    /**
     * 根据key得到boolean信息
     *
     */
    public static void clearReStart(Context context) {
        mShareConfigRestart = context.getSharedPreferences(SP_NAME_RESTART, Context.MODE_PRIVATE);
        if (null != mShareConfigRestart) {
            final Editor edit = mShareConfigRestart.edit();
            edit.clear();
            edit.commit();
        }
    }

    /**
     * 保存long值
     *
     * @param context
     * @param key
     * @param t
     */
    public static void saveLongByKey(Context context, String key, long t) {
        if (!TextUtils.isEmpty(key)) {
            mShareConfig = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                final Editor edit = mShareConfig.edit();
                edit.putLong(key, t);
                edit.commit();
            }
        }
    }

    /**
     * 保存String值
     *
     * @param key
     * @param value
     */
    public static void saveStringByKey(Context context, String key, String value) {
        if (!TextUtils.isEmpty(key) && context != null) {
            mShareConfig = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {

                final Editor edit = mShareConfig.edit();
                try {
                    if (!TextUtils.isEmpty(value))
                        value = SimpleCrypto.encrypt(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                edit.putString(key, value);
                edit.commit();
            }
        }
    }

    /**
     * 保存Int值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveIntByKey(Context context, String key, int value) {
        if (!TextUtils.isEmpty(key) && null != context) {
            mShareConfig = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            if (null != mShareConfig) {
                final Editor edit = mShareConfig.edit();
                edit.putInt(key, value);
                edit.commit();
            }
        }
    }

    /**
     * 分享功能
     *
     * @param context
     * @param shareMsg
     * @param activityTitle
     */
    public static void share(Context context, String shareMsg, String activityTitle) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, activityTitle);
        intent.putExtra(Intent.EXTRA_TEXT, shareMsg);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }


    /**
     * 判断某一服务是否在运行
     *
     * @param context
     * @param servicePackageName
     * @return
     */
    public static boolean isServiceAlive(Context context, String servicePackageName) {
        final ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(servicePackageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取屏幕分辨率
     *
     * @return displayMerics[0] = width displayMerics[1] = height
     */
    public static int[] getDisplayMetrics(Activity activity) {
        DisplayMetrics dm;// 屏幕分辨率容器
        final int[] displayMerics = new int[2];
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayMerics[0] = dm.widthPixels;
        displayMerics[1] = dm.heightPixels;

        return displayMerics;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 查询手机内非系统应用
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllNotSystemApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            // 判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    /**
     * 获取系统应用和预装应用
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllSystemApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            // 判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0 && pak.applicationInfo.uid > 10000) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    /**
     * 安装应用
     *
     * @param context
     * @param file    安装包地址
     */
    public static void installedApk(Context context, File file) {
        if (null == context || !file.exists() || file.length() == 0)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载应用
     *
     * @param context
     * @param packageName 应用包名
     */
    public static void unInstalledApk(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    /**
     * 打开应用
     *
     * @param context
     * @param packageName 应用包名
     */
    public static void openApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        // 如果该程序不可启动（像系统自带的包，有很多是没有入口的）会返回NULL
        if (intent != null) {
            context.startActivity(intent);
        } else {

        }
    }

    /**
     * copy file
     *
     * @param fromFile copied file, file in SD card absolute path
     * @param toFile   goal file, file in sd card absolute path
     */
    public static void copy(String fromFile, String toFile) {
        try {
            InputStream inputStream = new FileInputStream(fromFile);
            OutputStream outputStream = new FileOutputStream(toFile);
            byte[] b = new byte[1024];
            int readedLength = -1;
            while ((readedLength = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, readedLength);
            }
            new File(fromFile).delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteFile(File dir) {
        if (dir != null && !TextUtils.isEmpty(dir.getPath()) && dir.exists()) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    File temp = new File(dir, children[i]);
                    if (temp.isDirectory()) {
                        DeleteFile(temp);
                    } else {
                        boolean b = temp.delete();
                        if (b == false) {
                            Log.d("dir", "DELETE FAIL");
                        }
                    }
                }

                dir.delete();
            } else {
                Log.d("dir", dir.toString());
                dir.delete();
            }
        }
    }

    public static boolean isRoot() {
        File su = new File(SU_FILE);
        if (su.exists()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    /**
     * APP中显示用的版本号
     * @param context
     * @return
     */
    public static String getVersionShow(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /***
     * @param activity 当前activity
     * @param cls      跳转的activity
     * @param bundle   传值
     * @param isFinish 是否关闭当前activity
     */
    public static void startNextActivity(Activity activity, Class<?> cls, Bundle bundle, boolean isFinish) {
        if (null != activity)
            BaseApplication.mAppManager.addActivity(activity);
        Intent intent = new Intent(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isFinish)
            activity.finish();
        activity.overridePendingTransition(R.anim.lib_c_next_h_show, android.R.anim.fade_out);
        // activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    /***
     * @param activity 当前activity
     * @param cls      跳转的activity
     * @param bundle   传值
     * @param isFinish 是否关闭当前activity
     */
    public static void startToLastActivity(Activity activity, Class<?> cls, Bundle bundle, boolean isFinish) {
        if (null != activity)
            BaseApplication.mAppManager.addActivity(activity);
        Intent intent = new Intent(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isFinish)
            activity.finish();
        activity.overridePendingTransition(R.anim.lib_c_last_h_show, R.anim.lib_c_last_h_alpha);
    }


    /**
     * 带返回请求进行Activity跳转
     *
     * @param activity    当前Activity
     * @param cls         目标Activity
     * @param bundle      传值
     * @param requestCode Activity请求码
     */
    public static void startNextActivityForResult(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        if (null != activity)
            BaseApplication.mAppManager.addActivity(activity);
        Intent intent = new Intent(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.lib_c_next_h_show, android.R.anim.fade_out);
        // activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    /**
     * 验证手机格式
     * <p/>
     * "13\\d{9}"指前两位为1和3,后面跟任意9位; "14[57]\\d{8}"指前两位为1和4,第三位为5或7,后面跟任意8位;
     * <p/>
     * 以此类推
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[012356789]\\d{8}|17[012356789]\\d{8}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 验证车牌号
     * <p/>
     * 车牌号格式：汉字 + A-Z + 5位A-Z或0-9 （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）
     * <p/>
     * 这个正则表达式有局限性，比如第一位只限定是汉字，没限定只有34个省汉字缩写；车牌号不存在字母I和O，防止和1、0混淆；部分车牌无法分辨等等
     *
     * @param carnumber
     * @return
     */
    public static boolean isCarnumberNO(String carnumber) {
        String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
        if (TextUtils.isEmpty(carnumber))
            return false;
        else
            return carnumber.matches(carnumRegex);
    }

    /**
     * 自定义宽/高/图片质量处理图片
     *
     * @param width
     * @param height
     * @param quality
     * @return
     */
    @Deprecated
    public static String dealImgUrl(int width, int height, int quality){
        String imgUrl = "";
        if(width <= 10 || height <= 10)
            imgUrl = "";
        else
            imgUrl = "?imageMogr2/thumbnail/" + width +"x" + height + "/extent/" +  width +"x" + height  +"/background/d2hpdGU=/position/center/quality/"+ quality +"/format/webp";
        return imgUrl;
    }


    /**
     * 避免频繁弹出toast
     */
    private static MyToast toast;
    public static void showToast(Context context, Activity activity,String content) {
        if (toast == null) {
            toast = MyToast.makeText(context,activity,content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
    public static ContactBen getContactPhone(Cursor cursor, Context context) {
        ContactBen vo = new ContactBen();
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = 0;
        try {
            phoneNum = cursor.getInt(phoneColumn);
        } catch (Exception e) {
            return null;
        }

        // String phoneResult = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            vo.setName(name);

            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch (phone_type) {
                        case 2:
                            vo.setPhone(phoneNumber);
                            break;
                    }
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return vo;
    }
    public static void hideBottomUIMenu(Activity mContext ) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = mContext.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = mContext.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    static Context contxt;


    public static void showToastLong(Context context,Activity activity  ,String content) {
        if (toast == null) {
            toast = MyToast.makeText(context,activity,content, Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b
     *            byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static final String byte2hex(byte b[]) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }
    public static String  getMilionWith2Point(Double number){
        double temp=number/1000000;
        String format = String.format("%.2f", temp);
        return formatTosepara(format);

    }
    public static String  getPercentWith2Point(Double number){

        if (number.doubleValue()==0){
            return "0.00";
        }
        double temp=number*100;
        return String.format("%.2f", temp);
    }
    public static String  getDoubleWith2Point(Double number){

        if (number.doubleValue()==0){
            return "0.00";
        }
        return String.format("%.2f", number);
    }

    public static String getSign(String mText) {
        if (TextUtils.isEmpty(mText)) {
            return "";
        }
        return mText.equals("CNY") ? "元" : "美元";
    }

    public static String formatTosepara(String data) {

        if (data==null){
            return "";
        }

        double aDouble = Double.parseDouble(data);

        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(aDouble);
    }
    public static String formatTosepara(double data) {

        if (data==0){
            return "";
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(data);
    }
}
