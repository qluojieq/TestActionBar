package com.gree.testactionbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import com.gree.testactionbar.custom.VerticalTextView

class MainActivity2 : AppCompatActivity() {
    lateinit var textStyle: VerticalTextView
    lateinit var textStyle1: com.gree.testactionbar.VerticalTextView

    lateinit var textViewOrigin: TextViewCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        textStyle = findViewById(R.id.vertical_text)
        textStyle1 = findViewById(R.id.vertical_text1)
        var testStr = "无论我多么努力\n" +
                "仍无法留住你\n" +
                "点点的欢笑\n" +
                "此刻只可去追忆\n" +
                "无情地留下我\n" +
                "在风,中哭泣眼睛\n" +
                "告别了昨日的爱情\n" +
                "也许当初不该用情\n"
//                "如今不会再有痴情\n" +
//                "命运的注定\n" +
//                "何必错对要去算清\n" +
//                "也许当初她该用情\n" +
//                "如今相恋没有事情\n" +
//                "可惜只有我守住这份情\n" +
//                "无论我多么努力"
        textStyle.text = testStr
        textStyle1.text = testStr

    }
}