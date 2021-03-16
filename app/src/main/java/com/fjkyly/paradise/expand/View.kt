package com.fjkyly.paradise.expand

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull

fun inflate(
    @LayoutRes resource: Int, @NonNull root: ViewGroup, attachToRoot: Boolean = false
): View =
    LayoutInflater.from(root.context).inflate(resource, root, attachToRoot)

fun View.setRoundRectBg(color: Int = Color.WHITE, cornerRadius: Float = 15f.dp) {
    background = GradientDrawable().apply {
        setColor(color)
        setCornerRadius(cornerRadius)
    }
}