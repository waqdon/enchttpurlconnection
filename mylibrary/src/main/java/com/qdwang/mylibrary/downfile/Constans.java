package com.qdwang.mylibrary.downfile;

/**
 * author: create by qdwang
 * date: 2018/11/12 15:55
 * described：
 */
public class Constans {

    public final static int NONE = 0x1000;//无状态
    public final static int START = 0x1001;//准备下载
    public final static int PROGRESS = 0x1002;//下载中
    public final static int PAUSE = 0x1003;//暂停
    public final static int RESUME = 0x1004;//继续下载
    public final static int CANCEL = 0x1005;//取消
    public final static int RESTART = 0x1006;//重新下载
    public final static int FINISH = 0x1007;//下载完成
    public final static int ERROR = 0x1008;//下载出错
    public final static int NWAIT = 0x1009;//等待中
    public final static int DESTROY = 0x1010;//释放资源
}
