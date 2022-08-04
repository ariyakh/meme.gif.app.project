package com.gigfa.ariya

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val watermarkText: WatermarkText = WatermarkText(inputText)
            .setPositionX(0.5)
            .setPositionY(0.5)
            .setTextColor(Color.WHITE)
            .setTextFont(R.font.koodak)
            .setTextShadow(0.1f, 5, 5, Color.BLUE)
            .setTextAlpha(150)
            .setRotation(30)
            .setTextSize(20)

        WatermarkBuilder
            .create(this, backgroundBitmap)
            .loadWatermarkText(watermarkText) // use .loadWatermarkImage(watermarkImage) to load an image.
            .getWatermark()
            .setToImageView(imageView);

    }
}