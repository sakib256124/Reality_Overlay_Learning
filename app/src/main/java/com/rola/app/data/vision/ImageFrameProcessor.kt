package com.rola.app.data.vision

import android.graphics.Bitmap
import com.google.ar.core.Frame
import com.rola.app.data.ml.ImageProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageFrameProcessor @Inject constructor(
    private val imageProcessor: ImageProcessor,
) {
    fun frameToBitmap(frame: Frame): Bitmap? = imageProcessor.frameToBitmap(frame)
}
