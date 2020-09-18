package com.example.slide_app

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.StringBuilder
import java.util.*
import java.util.logging.Handler
import kotlin.concurrent.thread
import kotlin.concurrent.timer
import kotlin.math.log

class TouchUnlokeView: View {
    //半径
    private var radius = 0f
    //间隔
    var listener:MainActivity?=null

    private var padding = 0f

    var count:Int=0
    private var firstPassword:String?=null

    var orgpassword:String?=null
    //创建数组保存9个点的信息对象
    private val dotInfos= mutableListOf<DotInfo>()

    //创建数组保存被点亮的点的信息
    private val selectedItems = mutableListOf<DotInfo>()

    //保存上一个被点亮的点的信息
    private var lastedSelectedItem :DotInfo? = null

    //记录线条的路径
    private val linePath = Path()

    //创建线的画笔
    private val linePaint = Paint().apply {
        color = Color.RED    //画笔的颜色
        strokeWidth = 4f        //画笔的粗细
        style = Paint.Style.STROKE  //画笔的风格
    }

    //圆圈内部的白色遮盖的圆
    private val innerCirclePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    //记录触摸点的坐标
    private var endpoint = Point()

    //记录密码
    private val password = StringBuffer()
    //用代码创建
    constructor(context: Context):super(context){ }

    //xml创建
    constructor(context: Context,attrs:AttributeSet):super(context,attrs){  }

    //xml style
    constructor(context: Context,attrs: AttributeSet,style:Int):super(context,attrs,style){ }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //初始化
        init()
    }
    //绘制圆
    fun drawNineDot(canvas: Canvas?){
        for(info:DotInfo in dotInfos){
            canvas?.drawCircle(info.cx,info.cy,info.radius,info.paint)
            canvas?.drawCircle(info.cx,info.cy,info.radius-2,innerCirclePaint)
            if(info.isSelected){
                canvas?.drawCircle(info.cx,info.cy,info.innerCircleRadius,info.paint)
            }
        }
    }
    //绘制具体内容
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //canvas?.drawPath(dotPath,dotPaint)
        //绘制线
        if(!endpoint.equals(0,0)){
            canvas?.drawLine(lastedSelectedItem!!.cx,lastedSelectedItem!!.cy,
                endpoint.x.toFloat(),endpoint.y.toFloat(),
                linePaint)
        }
        canvas?.drawPath(linePath,linePaint)
        //绘制九个点
        drawNineDot(canvas)
    }
    private fun init(){
        //初始点的x坐标
       var cx = 0f
        //初始点的y坐标
        var cy = 0f
        //计算半径和间距
        if(measuredWidth>=measuredHeight){
            //半径
            radius = measuredHeight/5/2f
            //间距
            padding = (measuredHeight-3*radius*2)/4
            //第一个坐标点
            cx = (measuredWidth - measuredHeight)/2f + padding + radius
            cy = padding + radius
        }else{
            radius = measuredWidth/5/2f
            padding = (measuredWidth - 3*radius*2)/4
            cx = padding + radius
            cy = (measuredHeight - measuredWidth)/2f+padding +radius

        }
        //设置9个点的Path
        for (row in 0..2){
            for (colum in 0..2){
                DotInfo(
                    cx+colum*(2*radius+padding),
                    cy+row*(2*radius+padding),
                    radius,
                    row*3+colum+1
                ).also {
                    dotInfos.add(it)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                //判断点是否在某个矩形区域内
                containtPoint(x,y).also {
                    if(it != null) {
                        //点亮这个点
                        selectItem(it)
                        //lingPath的起点
                        linePath.moveTo(it.cx,it.cy)
                    }
                }
            }
            MotionEvent.ACTION_MOVE ->{
                containtPoint(x,y).also {
                    if(it != null) {
                        //触摸点在某个点的内部
                        if(!it.isSelected){
                            //没有点亮，判断是否为第一个点
                            if(lastedSelectedItem == null){
                                //是第一个点,就将这个点的中心点作为linePath的起点
                                linePath.moveTo(it.cx,it.cy)
                            }else{
                                //从前一个点 到当前点画线
                                linePath.lineTo(it.cx,it.cy)
                            }
                            selectItem(it)
                        }
                    }else{
                        //触摸点在外部
                        if(lastedSelectedItem != null){
                            endpoint.set(x!!.toInt(),y!!.toInt())
                            //需要刷新，不然线画不上去
                            invalidate()
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP ->{
                //判断是否为第一次
                if(orgpassword == null){
                    //判断是不是设置密码的第一次
                    if(firstPassword == null){
                        //记录第一次的密码
                        firstPassword = password.toString()
                        listener!!.mAlern.text = "请确认密码图案"
                    }else{
                        //确认密码
                        comparePassword(firstPassword!!,password.toString())
                    }
                }else{
                    //确认密码

                    comparePasswordtwo(orgpassword!!,password.toString())
                }
                    reset()
            }
        }
        return true
    }

    //判断两次密码是否相同
    private fun comparePassword(first:String,second:String){
        //确认密码
        if(first == second){
            Toast.makeText(listener!!,"密码设置成功",Toast.LENGTH_SHORT).show()
            //orgpassword = password.toString()
            //listener!!.mAlern.text = "请输入解锁图案"
            SharedPreferenceUtill.getInstance(listener!!).savePassword(first)

            listener!!.finish()
        }else{
            listener!!.mAlern.text="两次密码不一致，请重新输入"
            firstPassword = null
        }
    }

    private fun comparePasswordtwo(first:String,second:String){
        //确认密码
        if(first == second){
            listener!!.mAlern.text = "验证密码成功"
            //Log.v("zy",orgpassword)
            listener!!.junpTonextActivity()
            //SharedPreferenceUtill.getInstance(listener!!).savePassword(first)
        }else{
            count++
            if(count<=3){
                    Toast.makeText(listener!!,"密码错误",Toast.LENGTH_SHORT).show()
                    listener!!.mAlern.text="请重新输入"
            }else{
                Toast.makeText(listener!!,"输入密码错误次数过多,30秒后再重新操作",Toast.LENGTH_SHORT).show()
                listener!!.finish()
            }
        }
    }
    //重设
    private fun reset(){
        for(item:DotInfo in selectedItems){
            item.isSelected = false
        }
        //线条重设
        linePath.reset()
        invalidate()
        //清空
        selectedItems.clear()
        //清空密码
        password.delete(0,password.length)
    }
     fun containtPoint(x:Float?,y:Float?):DotInfo?{
        for(item:DotInfo in dotInfos){
            if(item.rect.contains(x!!.toInt(),y!!.toInt())){
                return item
            }
        }
        return null
    }
    //点亮一个点
     fun selectItem(item:DotInfo){
        //改变颜色
        item.isSelected = true
        //立即刷新，重新绘制
        invalidate()
        //保存点亮的点
        selectedItems.add(item)
        //记录这个点
        lastedSelectedItem = item
        //然后马上设置endPoint为空
        endpoint.set(0,0)
        //记录当前密码
        password.append(item.tag)
    }
    fun Setlistener1(mylistener1:MainActivity){
        this.listener = mylistener1
    }
}