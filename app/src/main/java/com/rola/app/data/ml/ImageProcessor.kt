package com.rola.app.data.ml

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import com.google.ar.core.Frame
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import javax.inject.Inject

class ImageProcessor @Inject constructor() {
    fun frameToBitmap(frame: Frame): Bitmap? {
        val image = runCatching { frame.acquireCameraImage() }.getOrNull() ?: return null
        return image.use { cameraImage ->
            cameraImage.toBitmap()
        }
    }

    fun toModelInput(
        bitmap: Bitmap,
        inputWidth: Int,
        inputHeight: Int,
        dataType: DataType,
    ): TensorImage {
        val tensorImage = TensorImage(dataType)
        tensorImage.load(bitmap)

        val processor = org.tensorflow.lite.support.image.ImageProcessor.Builder()
            .add(ResizeOp(inputHeight, inputWidth, ResizeOp.ResizeMethod.BILINEAR))
            .apply {
                if (dataType == DataType.FLOAT32) {
                    add(NormalizeOp(0f, 255f))
                }
            }
            .build()

        return processor.process(tensorImage)
    }

    private fun Image.toBitmap(): Bitmap? {
        if (format != ImageFormat.YUV_420_888) return null

        val nv21 = yuv420ToNv21(this)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val outputStream = ByteArrayOutputStream()
        val success = yuvImage.compressToJpeg(Rect(0, 0, width, height), JPEG_QUALITY, outputStream)
        if (!success) return null

        val jpegBytes = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)
    }

    private fun yuv420ToNv21(image: Image): ByteArray {
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 4
        val nv21 = ByteArray(ySize + uvSize * 2)

        val yPlane = image.planes[0]
        val uPlane = image.planes[1]
        val vPlane = image.planes[2]

        copyYPlane(yPlane.buffer, yPlane.rowStride, width, height, nv21)
        copyUvPlanes(
            uBuffer = uPlane.buffer,
            vBuffer = vPlane.buffer,
            uRowStride = uPlane.rowStride,
            vRowStride = vPlane.rowStride,
            uPixelStride = uPlane.pixelStride,
            vPixelStride = vPlane.pixelStride,
            width = width,
            height = height,
            output = nv21,
            outputOffset = ySize,
        )
        return nv21
    }

    private fun copyYPlane(
        buffer: ByteBuffer,
        rowStride: Int,
        width: Int,
        height: Int,
        output: ByteArray,
    ) {
        var outputOffset = 0
        for (row in 0 until height) {
            val rowStart = row * rowStride
            buffer.position(rowStart)
            buffer.get(output, outputOffset, width)
            outputOffset += width
        }
    }

    private fun copyUvPlanes(
        uBuffer: ByteBuffer,
        vBuffer: ByteBuffer,
        uRowStride: Int,
        vRowStride: Int,
        uPixelStride: Int,
        vPixelStride: Int,
        width: Int,
        height: Int,
        output: ByteArray,
        outputOffset: Int,
    ) {
        var index = outputOffset
        val chromaWidth = width / 2
        val chromaHeight = height / 2

        for (row in 0 until chromaHeight) {
            for (col in 0 until chromaWidth) {
                val vuIndex = row * vRowStride + col * vPixelStride
                val uuIndex = row * uRowStride + col * uPixelStride
                output[index++] = vBuffer.get(vuIndex)
                output[index++] = uBuffer.get(uuIndex)
            }
        }
    }

    private companion object {
        const val JPEG_QUALITY = 80
    }
}
