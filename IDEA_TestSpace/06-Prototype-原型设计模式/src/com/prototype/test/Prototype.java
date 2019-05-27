package com.prototype.test;

import com.prototype.pojo.User;

import java.io.*;

public class Prototype implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private String str;
    private User user;

    /*浅复制*/
    @Override
    public Object clone() throws  CloneNotSupportedException{
        Prototype pro = (Prototype) super.clone();
        return pro;
    }

    /*深复制*/
    public Object deepClone() throws IOException,ClassNotFoundException{
        /*写入当前对象的二进制流*/
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        /*读出二进制流产生的新对象*/
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

