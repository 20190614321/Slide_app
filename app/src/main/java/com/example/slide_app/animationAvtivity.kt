package com.example.slide_app

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_animation_activity.*

class animationAvtivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_activity)
    }

    override fun onStart() {
        super.onStart()
        manuAnim()
    }
    //手动创建动画
    fun manuAnim(){
        AnimationDrawable().apply {
            addFrame(getDrawable(R.drawable.first)!!,200)
            addFrame(getDrawable(R.drawable.second)!!,200)
            addFrame(getDrawable(R.drawable.three)!!,200)
            addFrame(getDrawable(R.drawable.four)!!,200)
            addFrame(getDrawable(R.drawable.five)!!,200)
            addFrame(getDrawable(R.drawable.six)!!,200)
            addFrame(getDrawable(R.drawable.seven)!!,200)
            addFrame(getDrawable(R.drawable.eight)!!,200)
            //启动
            animationView.setImageDrawable(this)
            start()
        }
    }
}
