package com.prototype.test;

import com.prototype.pojo.User;

public class Test {
    public static void main(String args[]){
        Prototype pro = new Prototype();
        pro.setUser(new User("name",16,"ÄÐ"));
        pro.setStr("pro1");
        System.out.println(pro.hashCode());

        try{
            Prototype pro2 = (Prototype)pro.deepClone();
            System.out.println(pro2.getStr());
            System.out.println(pro2.getUser());
            System.out.println(pro2.hashCode());

            Prototype pro3 = (Prototype)pro.clone();
            System.out.println(pro3.getStr());
            System.out.println(pro3.getUser());
            System.out.println(pro3.hashCode());

        }catch (Exception e){
            e.printStackTrace();
        }



    }
}
