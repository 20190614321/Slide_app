package com.example.slide_app

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtill private constructor(){
    private var FILE_NAME = "password"
    private var KEY = "passwordKey"
    companion object{
        private var mContext:Context ?= null
        private var instance:SharedPreferenceUtill ?= null  //内部存储对象

        //外部调用函数
        fun getInstance(context: Context):SharedPreferenceUtill{
            mContext = context
            //判断，为空就创建
            if(instance == null){
                synchronized(this){
                    instance = SharedPreferenceUtill()
                }
            }
            //不为空，就返回instance
            return instance!!
        }
    }
    //保存密码
    fun savePassword(pwd:String){
        //获取preference对象
        val sharedPreference:SharedPreferences? = mContext?.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
        //获取edit对象，写数据
        val edit:SharedPreferences.Editor? = sharedPreference?.edit()
        //写入数据
        edit?.putString(KEY,pwd)
        //提交
        edit?.apply()
    }
    //获取密码
    fun getPassword():String?{
        //获取preference对象
        val sharedPreference:SharedPreferences? = mContext?.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
        return sharedPreference?.getString(KEY,null)
    }
}