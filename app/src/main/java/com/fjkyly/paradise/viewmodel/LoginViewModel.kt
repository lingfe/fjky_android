package com.fjkyly.paradise.viewmodel

import android.app.Application
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    fun inputAccountNum(view: EditText) {
        val accountNum = view.text

    }
}