package com.fjkyly.paradise.other

import android.graphics.*
import android.graphics.drawable.Drawable

class RoundImageDrawable(mBitmap: Bitmap, radius: Float) : Drawable() {

    private val mPaint: Paint
    private val mWidth: Int
    private val mRadius: Float

    init {
        val bitmapShader = BitmapShader(
            mBitmap, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.shader = bitmapShader
        mWidth = mBitmap.width.coerceAtMost(mBitmap.height)
        mRadius = radius
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, mWidth.toFloat(), mWidth.toFloat(), mRadius, mRadius, mPaint)
    }

    override fun getIntrinsicWidth(): Int {
        return mWidth
    }

    override fun getIntrinsicHeight(): Int {
        return mWidth
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}