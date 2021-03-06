/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.common.lib.common.etoast2;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.common.lib.common.rom.RomEToastUtils;
import com.common.lib.common.rom.HuaweiUtils;
import com.common.lib.common.rom.MeizuUtils;
import com.common.lib.common.rom.MiuiUtils;
import com.common.lib.common.rom.OppoUtils;
import com.common.lib.common.rom.QikuUtils;

import java.lang.reflect.Method;

public class OpPermissionUtils {
    private static final String TAG = "OpPermissionUtils";

    public static boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomEToastUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomEToastUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomEToastUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomEToastUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomEToastUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    private static boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private static boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private static boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private static boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private static boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private static boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomEToastUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }
}
