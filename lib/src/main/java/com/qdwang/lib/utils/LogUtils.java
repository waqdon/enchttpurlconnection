package com.qdwang.lib.utils;

import android.util.Log;

import com.qdwang.lib.utils.configs.DebugConfigs;
import java.util.Locale;

/**
 * 创建时间：2017/11/29
 * 编写人：qdwang
 * 功能描述：
 */

public class LogUtils {
    private static String TAG = "YGph";

    public static void LogOut(String info) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && info != null) {
            StackTraceElement stack[] = (new Throwable()).getStackTrace();
            if(stack.length > 1){
                StackTraceElement s = stack[1];
                String[] names= s.getClassName().split("\\.");
                Log.d("Kalaok",names[names.length-1]+"第"+s.getLineNumber()+"行:---------"+ info);
            }else{
                Log.d("Kalaok", info);
            }
        }
    }
    public static void LogOut(String tag, String info) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && info != null) {
            StackTraceElement stack[] = (new Throwable()).getStackTrace();
            if(stack.length > 1){
                StackTraceElement s = stack[1];
                String[] names= s.getClassName().split("\\.");
                Log.d(tag,names[names.length-1]+"第"+s.getLineNumber()+"行-->"+info);
            }else{
                Log.d(tag, info);
            }
        }
    }
    public static void LogOutE(String tag, String info) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && info != null) {
            StackTraceElement stack[] = (new Throwable()).getStackTrace();
            if(stack.length > 1){
                StackTraceElement s = stack[1];
                String[] names= s.getClassName().split("\\.");
                Log.e(tag,names[names.length-1]+"第"+s.getLineNumber()+"行-->"+info);
            }else{
                Log.e(tag, info);
            }
        }
    }
    public static void LogOutDetail(String tag, String info) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && info != null) {
            StackTraceElement stack[] = (new Throwable()).getStackTrace();
            if(stack.length > 3){
                StringBuffer buffer = new StringBuffer();
                buffer.append(info);
                buffer.append("<--");
                String[] names= stack[1].getClassName().split("\\.");
                buffer.append(names[names.length-1]+"第"+stack[1].getLineNumber()+"行<--");
                names= stack[2].getClassName().split("\\.");
                buffer.append(names[names.length-1]+"第"+stack[2].getLineNumber()+"行<--");
                names= stack[3].getClassName().split("\\.");
                buffer.append(names[names.length-1]+"第"+stack[3].getLineNumber()+"行");
                Log.d(tag,buffer.toString());
            }else{
                Log.d(tag, info);
            }
        }
    }

    public static void d(String format, Object... args) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && args != null) {
            Log.d(TAG, buildMessage(format, args));
        }
    }

    public static void d1(String arg) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug)) {
            Log.d(TAG, arg);
        }
    }

    public static void d_tag(String tag, String format, Object... args) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && args != null) {
            Log.d(tag, buildMessage(format, args));
        }
    }

    public static void e(String format, Object... args) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && args != null) {
            Log.e(TAG, buildMessage(format, args));
        }
    }

    public static void e(String tag, String format, Object... args) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && args != null) {
            Log.e(tag, buildMessage(format, args));
        }
    }

    public static void dDetail(String tag, String info) {
        if ((DebugConfigs.isDebug || DebugConfigs.isSingnDubug) && info != null) {
            StackTraceElement stack[] = (new Throwable()).getStackTrace();
            if (stack.length > 3) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(info);
                buffer.append("<--");
                String[] names = stack[1].getClassName().split("\\.");
                buffer.append(names[names.length - 1] + "第" + stack[1].getLineNumber() + "行<--");
                names = stack[2].getClassName().split("\\.");
                buffer.append(names[names.length - 1] + "第" + stack[2].getLineNumber() + "行<--");
                names = stack[3].getClassName().split("\\.");
                buffer.append(names[names.length - 1] + "第" + stack[3].getLineNumber() + "行");
                Log.d(tag, buffer.toString());
            } else {
                Log.d(tag, info);
            }
        }
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg=format;
        try {
            msg = (args == null) ? format : String.format(Locale.US, format, args);
        }catch (Exception e){
            msg=format;
        }

        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of VolleyLog.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtils.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName() + " line:" + trace[i].getLineNumber();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s-----> %s", Thread.currentThread().getId(), caller, msg);
    }
}
