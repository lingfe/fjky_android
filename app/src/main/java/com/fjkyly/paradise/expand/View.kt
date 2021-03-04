package com.fjkyly.paradise.expand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull

fun inflate(
    @LayoutRes resource: Int, @NonNull root: ViewGroup, attachToRoot: Boolean = false
): View =
    LayoutInflater.from(root.context).inflate(resource, root, attachToRoot)
