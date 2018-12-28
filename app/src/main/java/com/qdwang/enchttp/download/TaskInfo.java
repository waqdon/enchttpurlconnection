package com.qdwang.enchttp.download;

/**
 * author: create by qdwang
 * date: 2018/11/13 17:16
 * described：
 */
public class TaskInfo {
    private String name;//文件名
    private String path;//文件路径
    private String url;//链接
    private long contentLen;//文件总长度
    /**
     * 迄今为止java虚拟机都是以32位作为原子操作，而long与double为64位，当某线程
     * 将long/double类型变量读到寄存器时需要两次32位的操作，如果在第一次32位操作
     * 时变量值改变，其结果会发生错误，简而言之，long/double是非线程安全的，volatile
     * 关键字修饰的long/double的get/set方法具有原子性。
     */
    private volatile long completedLen;//已完成长度

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public long getContentLen() {
        return contentLen;
    }

    public void setContentLen(long contentLen) {
        this.contentLen=contentLen;
    }

    public long getCompletedLen() {
        return completedLen;
    }

    public void setCompletedLen(long completedLen) {
        this.completedLen=completedLen;
    }
}
