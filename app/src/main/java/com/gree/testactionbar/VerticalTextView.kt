package com.gree.testactionbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.TextView
import java.util.regex.Pattern

/**
 *    Created by Brandon on 2021/6/9.
 **/
@SuppressLint("AppCompatCustomView")
class VerticalTextView : TextView {
    var verticalSpan = 10f
    var horizontalSpan = 6f
    var readOrient = true//"right"
    var textBounds = intArrayOf(0, 0)

    //    var isAutoFeed = false
    var color: IntArray = intArrayOf(Color.RED, Color.YELLOW)
    var position: FloatArray = floatArrayOf(0f, 1f)


    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        // view的初始测量宽高(包含padding)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        // 粗略计算文字的最大宽度和最大高度，用于修正最后的测量宽高
        // 粗略计算文字的最大宽度和最大高度，用于修正最后的测量宽高
        textBounds = getTextRoughSize(
            heightSize,
            verticalSpan, horizontalSpan
        )


        val measuredWidth: Int =
            if (widthMode == MeasureSpec.AT_MOST
                || widthMode == MeasureSpec.UNSPECIFIED
            ) textBounds[0] else widthSize


        val measureHeight: Int = if (heightMode == MeasureSpec.AT_MOST
            || heightMode == MeasureSpec.UNSPECIFIED
        ) textBounds[1] else heightSize

        setMeasuredDimension(measuredWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        paint.shader =
            LinearGradient(0f, 0f, textBounds[0].toFloat(), textBounds[1].toFloat(), color, position, Shader.TileMode.CLAMP)
        var str = text
        if (str.isEmpty()) {
            return
        }
        var currentX = width.toFloat() - textSize

        if (readOrient) {
            currentX = 0f
        }
        var currentY = textSize
        for (char in str) {
            canvas?.drawText(char.toString(), currentX, currentY, paint)
            if (char.toString() == "\n") {
                if(readOrient){
                    currentX += (textSize+horizontalSpan)
                }else{
                    currentX -= (textSize+horizontalSpan)
                }
                currentY = textSize
            } else {
                currentY += (textSize+verticalSpan)
            }
        }
    }

    /**
     * 粗略计算文本的宽度和高度(包含padding)，用于修正最后的测量宽高
     *
     * @param oriHeightSize    初始测量高度 必须大于0。当等于0时，用屏幕高度代替
     * @param lineSpacingExtra
     * @param charSpacingExtra
     * @return int[textWidth, textHeight]
     */
    private fun getTextRoughSize(
        oriHeightSize: Int, lineSpacingExtra: Float,
        charSpacingExtra: Float
    ): IntArray {

        // 将文本用换行符分隔，计算粗略的行数
        val subTextStr = text.toString().split("\n").toTypedArray()
        var textLines = 0
        // 用于计算最大高度的目标子段落
        var targetSubPara = ""
        var tempLines = 1
        var tempLength = 0f
        // 计算每个段落的行数，然后累加
        for (aSubTextStr in subTextStr) {
            // 段落的粗略长度(字符间距也要考虑进去)
            val subParagraphLength = aSubTextStr.length * (textSize + charSpacingExtra)
            // 段落长度除以初始测量高度，得到粗略行数
            var subLines = Math.ceil(
                (subParagraphLength
                        / Math.abs(oriHeightSize - paddingTop - paddingBottom)).toDouble()
            )
                .toInt()
            if (subLines == 0) subLines = 1
            textLines += subLines
            // 如果所有子段落的行数都为1,则最大高度为长度最长的子段落长度；否则最大高度为oriHeightSize；
            if (subLines == 1 && tempLines == 1) {
                if (subParagraphLength > tempLength) {
                    tempLength = subParagraphLength
                    targetSubPara = aSubTextStr
                }
            }
            tempLines = subLines
        }
        // 计算文本粗略高度，包括padding
        var textHeight = paddingTop + paddingBottom
        if (textLines > subTextStr.size) textHeight = oriHeightSize else {
            // 计算targetSubPara长度作为高度
            for (i in 0 until targetSubPara.length) {
                val char_i = text.toString()[i].toString()
                // 区别标点符号 和 文字
                if (isUnicodeSymbol(char_i)) {
                    textHeight += (1.4f * getCharHeight(
                        char_i,
                        paint
                    ) + charSpacingExtra).toInt()
                } else {
                    textHeight += (textSize + charSpacingExtra).toInt()
                }
            }
        }
        // 计算文本的粗略宽度，包括padding，
        val textWidth = paddingLeft + paddingRight +
                ((textLines + 1) * textSize + lineSpacingExtra * (textLines - 1)).toInt()

        return intArrayOf(textWidth, textHeight)
    }

    private fun getCharHeight(target_char: String, paint: Paint): Float {
        val rect = Rect()
        paint.getTextBounds(target_char, 0, 1, rect)
        return rect.height().toFloat()
    }

    private fun isUnicodeSymbol(str: String): Boolean {
        val regex = ".*[_\"`!@#$%^&*()|{}':;,\\[\\].<>/?！￥…（）【】‘’；：”“。，、？︵ ︷︿︹︽﹁﹃︻︶︸﹀︺︾ˉ﹂﹄︼]$+.*"
        val m = Pattern.compile(regex).matcher(str)
        return m.matches()
    }


}