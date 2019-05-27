package com.singleton.test;

public class Singleton {
    private static Singleton instance = null;
    private Integer a;

    /**
     * 私有构造方法
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
     * 线程安全保护
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
