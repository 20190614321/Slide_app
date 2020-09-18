package com.example.slide_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        touchUnlokeView.Setlistener1(this)
        SharedPreferenceUtill.getInstance(this).getPassword().also {
            if(it == null){
                mAlern.text = "请设置密码图案"
            }else{
                mAlern.text = "请绘制解锁图案"
                touchUnlokeView.orgpassword = it
                Log.v("zy","-------it:$it-------------")
            }
        }
    }
    fun junpTonextActivity(){
        startActivity(Intent(this,nextAvtivity::class.java))
    }
}
