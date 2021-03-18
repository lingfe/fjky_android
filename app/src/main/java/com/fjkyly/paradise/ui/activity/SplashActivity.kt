package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.expand.mainHandler
import com.fjkyly.paradise.expand.startActivity

/**
 * 启动页界面
 */
class SplashActivity : MyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        callAllInit()
    }

    override fun initEvent() {
        mainHandler.postDelayed({
            startActivity<LoginActivity>()
            finish()
        }, DELAY_JUMP_TIME)
    }

    companion object {
        const val DELAY_JUMP_TIME = 1000L
        // const val DELAY_JUMP_TIME = 0L
    }
}