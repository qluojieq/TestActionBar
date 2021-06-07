package com.gree.testactionbar

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView


/**
 *    Created by Brandon on 2021/6/7.
 **/

class Img : AppCompatImageView {
    private var xx = 1f
    private var yy = 1f
    private var now = 0.0

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) {


    }

    override fun onDraw(canvas: Canvas) {
        canvas.scale(xx, yy, (getWidth() / 2).toFloat(), (getHeight() / 2).toFloat())
        canvas.save()
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.pointerCount > 1) {
            val an = (Math.sqrt(Math.pow((event.getX(0) - event.getX(1)).toDouble(), 2.0))
                    + Math.sqrt(Math.pow((event.getY(0) - event.getY(1)).toDouble(), 2.0)))
            if (now != 0.0) {
                if (an > now) {
                    if (xx < 2) {
                        xx += 0.05f
                        yy += 0.05f
                        invalidate()
                    }
                } else {
                    if (xx > 0.2) {
                        xx -= 0.05f
                        yy -= 0.05f
                        invalidate()
                    }
                }
            }
            now = an
            return true
        } else {
//            return super.onTouchEvent(event)
        }
        return true
    }


}

