package com.gree.testactionbar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.view.View.OnTouchListener

import android.widget.ImageView

/**
 *    Created by Brandon on 2021/6/7.
 **/
@SuppressLint("AppCompatCustomView")
class ScaleImage : ImageView, OnScaleGestureListener, OnTouchListener {
    lateinit var gestureDetector: GestureDetector
    lateinit var scaleGestureDetector: ScaleGestureDetector

    constructor(context: Context) : this(context, null) {
        initData(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        initData(context)
    }

    private fun initData(context: Context) {
        scaleType = ScaleType.MATRIX
        scaleGestureDetector = ScaleGestureDetector(context, this)
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                Log.e("brandon", "$distanceX")
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

        })

        setOnTouchListener(this)

    }


    override fun onScale(detector: ScaleGestureDetector?): Boolean {

        Log.e("brandon", "detector${detector?.currentSpan}")
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }


    override fun onScaleEnd(detector: ScaleGestureDetector?) {

    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        return if (gestureDetector.onTouchEvent(event)) {
            true
        } else {
            scaleGestureDetector.onTouchEvent(event)
        }

    }

}
