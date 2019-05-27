package com.prototype.test;

import com.prototype.pojo.User;

import java.io.*;

public class Prototype implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private String str;
    private User user;

    /*ǳ����*/
    @Override
    public Object clone() throws  CloneNotSupportedException{
        Prototype pro = (Prototype) super.clone();
        return pro;
    }

    /*���*/
    public Object deepClone() throws IOException,ClassNotFoundException{
        /*д�뵱ǰ����Ķ�������*/
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        /*�������������������¶���*/
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

