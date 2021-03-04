package com.fjkyly.paradise.expand

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

/**
 * 运行在子线程中的 Handler
 */
val threadHandler: Handler
    get() {
        val handlerThread = HandlerThread("back")
        handlerThread.start()
        return Handler(handlerThread.looper)
    }

/**
 * 运行在 UI 线程的 Handler
 */
val mainHandler: Handler
    get() = Handler(Looper.getMainLooper())