package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.fjkyly.paradise.HomeActivity
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.expand.*

class SplashActivity : MyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        callAllInit()
    }

    override fun initEvent() {
        mainHandler.postDelayed({
            val userSettingSp = getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE)
            val isAutoLogin = userSettingSp.getBoolean(AUTO_LOGIN_STATUS, false)
            val userToken = userSettingSp.getString(USER_TOKEN, "") ?: ""
            val userId = userSettingSp.getString(USER_ID, "") ?: ""
            Log.d(TAG, "initEvent: ===>userToken：$userToken")
            Log.d(TAG, "initEvent: ===>userId：$userId")
            if (isAutoLogin && userToken.isNotEmpty() && userId.isNotEmpty()) {
                startActivity<HomeActivity>()
            } else {
                startActivity<LoginActivity>()
            }
            finish()
        }, DELAY_JUMP_TIME)
    }

    companion object {
        private const val TAG = "SplashActivity"
        const val DELAY_JUMP_TIME = 1000L
        // const val DELAY_JUMP_TIME = 0L
    }
}