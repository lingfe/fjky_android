package com.fjkyly.paradise.expand

import android.widget.Toast
import com.fjkyly.paradise.base.App

fun simpleToast(text: String) {
    Toast.makeText(App.appContext, text, Toast.LENGTH_SHORT).show()
}

fun simpleToast(text: Int) {
    Toast.makeText(App.appContext, text, Toast.LENGTH_SHORT).show()
}