package com.example.slide_app

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_next_avtivity.*

class nextAvtivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_avtivity)
        btn.setOnClickListener{
            //旋转
            ObjectAnimator.ofFloat(textHuan,"rotation",textHuan.rotation+360f).apply {
                duration = 1000
                start()
            }
            ObjectAnimator.ofFloat(textYing,"rotation",textYing.rotation+360f).apply {
                duration = 1000
                start()
            }
            ObjectAnimator.ofFloat(textDeng,"rotation",textDeng.rotation+360f).apply {
                duration = 1000
                start()
            }
            ObjectAnimator.ofFloat(textLu,"rotation",textLu.rotation+360f).apply {
                duration = 1000
                start()
            }
        }
        mBttn.setOnClickListener{
            val intent = Intent(this,animationAvtivity::class.java)

            startActivity(intent)
        }
    }
}
