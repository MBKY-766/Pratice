package com.itheima.domain;

import java.io.Serial;
import java.io.Serializable;
//使用序列化流要实现序列化接口Serializable

/**
 * Serializable接口里面时没有抽象方法，标记型接口
 * 一旦实现了这个接口，那么就表示当前的Daily类可以被序列化
 * 理解：
 *      相当于一个物品的合格证
 */
public class Daily implements Serializable {
    @Serial
    private static final long serialVersionUID = 3738563697793064171L;
    private int ID; //编号
    private String title; //标题
    private String content; //内容

    public Daily() {
    }

    public Daily(int ID, String title, String content) {
        this.ID = ID;
        this.title = title;
        this.content = content;
    }

    /**
     * 获取
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * 设置
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * 获取
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        return "Daily{ID = " + ID + ", title = " + title + ", content = " + content + "}";
    }
}
