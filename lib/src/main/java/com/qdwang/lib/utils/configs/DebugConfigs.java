package com.qdwang.lib.utils.configs;

/**
 * 创建时间：2017/11/29
 * 编写人：qdwang
 * 功能描述：debug版本配置
 */

public class DebugConfigs {

    /**
     * 是否是debug配置模式
     */
    public static boolean isDebug = true;

    public static boolean isSingnDubug = false; //是否正式环境下也打开调试

    /**
     * 是否是一个服务器主机，一般情况下是多个服务器主机
     */
    public static boolean isSingleHost = false;
}
