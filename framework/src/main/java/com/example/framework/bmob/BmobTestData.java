package com.example.framework.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 测试云函数的类
 */
public class BmobTestData extends BmobObject {

    private String name;
    private int  age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
