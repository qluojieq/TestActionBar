package com.gree.testactionbar

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

/**
 *    Created by Brandon on 2021/6/7.
 **/
class ScaleImageView : AppCompatImageView {

    var currentMatrix: Matrix = Matrix()
    var currentRect: RectF = RectF()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        currentMatrix.reset()
        updateBounds()
        currentMatrix.postScale(-1f,1f,currentRect.centerX(),currentRect.centerY())
        transform()
    }

    fun transform(){
        imageMatrix = currentMatrix
        updateBounds()
    }

    private fun updateBounds() {
        drawable?.let {
            currentRect.set(drawable.bounds)
            currentMatrix.mapRect(currentRect)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return super.onTouchEvent(event)
    }
}