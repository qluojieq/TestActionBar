package com.gree.testactionbar

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.atan2

/**
 *    Created by Brandon on 2021/6/7.
 **/
const val PADDING = 20
const val ROTATABLE = false
const val SCALABLE = true
const val TRANSLATABLE = true

class ScaleImageView : AppCompatImageView {

    private var currentMatrix: Matrix = Matrix()
    var currentRect: RectF = RectF()

    private var scalable = false
    var rotatable = false
    var translatable = false

    var oldSinglePoint: PointF = PointF()
    var oldRotateVector: PointF = PointF()
    var oldScaleVector: PointF = PointF()

    var oldScale:Float = 0f

    constructor(context: Context) : this(context, null) {

    }


    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        scaleType = ScaleType.MATRIX
        this.setOnClickListener {
            Log.e("brandon","clicked")
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        currentMatrix.reset()
        updateBounds()
        var scaleFactor = Math.min(width / currentRect.width(), height / currentRect.height())
        currentMatrix.postScale(
            scaleFactor,
            scaleFactor,
            currentRect.centerX(),
            currentRect.centerY()
        )
        currentMatrix.postTranslate(pivotX - currentRect.centerX(), pivotY - currentRect.centerY())
        transform()
    }

    private fun transform() {
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
//        Log.e("Brandon","event$translatable")
        when (event?.actionMasked) {
            ACTION_DOWN -> {
                checkPointerCount(event.pointerCount)
                if (translatable) {
                    oldSinglePoint.set(event.x, event.y)
//                    return true
                }
            }
            ACTION_POINTER_DOWN -> {
                checkPointerCount(event.pointerCount)
                if (rotatable) {
                    oldRotateVector.set(
                        event.getX(0) - event.getX(1),
                        event.getY(0) - event.getY(1)
                    )
                }
                if (scalable) {
                    oldScaleVector.set(
                        event.getX(0) - event.getX(1),
                        event.getY(0) - event.getX(1)
                    )
                    oldScale = distance(event)
                }
            }
            ACTION_POINTER_UP -> {
                when (event.pointerCount) {
                    2 -> {
                        translatable = false
                        scalable = false
                        rotatable = false
                    }
                    3 -> {
                        translatable = false
                        scalable = SCALABLE
                        rotatable = ROTATABLE
                    }
                }

            }
            ACTION_MOVE -> {
                if (translatable) {
                    updateTranslate(event)
                }
                if (scalable) {
                    updateScale(event)
                }
                if (rotatable) {
                    updateRotate(event)
                }
                transform()
            }
            ACTION_UP -> {
                oldSinglePoint.set(0f, 0f)
            }

        }
        return super.onTouchEvent(event)
    }

    //平移
    private fun updateTranslate(event: MotionEvent) {
        var dx = event.x - oldSinglePoint.x
        var dy = event.y - oldSinglePoint.y
        if (currentRect.left + dx > width - PADDING || currentRect.right + dx < 20
            || currentRect.top + dy > height - 20 || currentRect.bottom + dy < 20
        ) {
            return
        } else {
            currentMatrix.postTranslate(dx, dy)
        }
        oldSinglePoint.set(event.x, event.y)
    }

    // 旋转
    private fun updateRotate(event: MotionEvent) {

        var vector = PointF(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1))
        var oldTan = atan2(oldRotateVector.y.toDouble(), oldRotateVector.x.toDouble())
        var newTan = atan2(vector.y.toDouble(), vector.x.toDouble())
        var degree = Math.toDegrees(newTan - oldTan)
        currentMatrix.postRotate(degree.toFloat(), currentRect.centerX(), currentRect.centerY())
        oldRotateVector = vector
    }

    // 缩放
    private fun updateScale(event: MotionEvent) {

        var vector = PointF(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1))
//        var scaleFactor = vector.length() / oldScaleVector.length()
        var currentDis = distance(event)
        var scaleFactor = currentDis/oldScale
        var centerX = (event.getX(0) + event.getX(1)) / 2
        var centerY = (event.getY(0) + event.getY(1)) / 2
        currentMatrix.postScale(scaleFactor, scaleFactor, centerX, centerY)
        Log.e("Brandon", "scaleFactor $scaleFactor")
        oldScaleVector = vector
        oldScale = currentDis
    }


    // 设置转换状态
    private fun checkPointerCount(cunt: Int) {
        when (cunt) {
            1 -> {
                translatable = TRANSLATABLE
                scalable = false
                rotatable = false
            }
            2 -> {
                scalable = SCALABLE
                rotatable = ROTATABLE
                translatable = false
            }
            else -> {
                scalable = false
                rotatable = false
                translatable = false
            }
        }
    }

    private fun distance(event: MotionEvent?): Float {
        event?.let {
            val x = event.getX(0) - event.getX(1)
            val y = event.getY(0) - event.getY(1)
            var tem = Math.sqrt((x * x + y * y).toDouble()).toFloat()
            return  tem//两点间距离公式
        }
        return -1f
    }
}