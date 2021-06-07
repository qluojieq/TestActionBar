package com.gree.testactionbar

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var image: CustomImageView
    private lateinit var btn:Button
    private lateinit var btnSave:Button
    private lateinit var previewImage: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.img_scale)
        btn = findViewById(R.id.button)
        btnSave = findViewById(R.id.button2)
        previewImage = findViewById(R.id.previewImage)
        btnSave.setOnClickListener{
            var bitmap = Bitmap.createBitmap(900,900,Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
//            image.layout(0,0,300,300)
            image.draw(canvas)

            previewImage.setImageBitmap( Bitmap.createBitmap(bitmap,300,300,300,300))
        }
        btn.setOnClickListener {
            var intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.setPackage("com.android.gallery3d")
            intent.putExtra("Multiselect", false)
            intent.putExtra("needShowVideo", false)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

    }

    /**
     * Dispatch incoming result to the correct fragment.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> {
                if (resultCode == Activity.RESULT_OK) {
                    image.setImageURI(data?.data)
                }
            }
        }

    }
}