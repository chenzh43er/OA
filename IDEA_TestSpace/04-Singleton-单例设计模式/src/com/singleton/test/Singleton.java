package com.singleton.test;

public class Singleton {
    private static Singleton instance = null;
    private Integer a;

    /**
     * ˽�й��췽��
     */
    private Singleton(){
        a=1;
    }


    /*public static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }*/

    /**
     * �̰߳�ȫ����
     * @return
     */
    public static Singleton getInstance(){
        if(instance == null){
            synchronized (instance){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }



    public Integer getA(){
        return  a;
    }
}
