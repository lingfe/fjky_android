package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityBrowserBinding
import com.fjkyly.paradise.ui.fragment.BrowserFragment

class BrowserActivity : MyActivity() {

    private var _binding: ActivityBrowserBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initData() {
        mBinding.run {
            val urlLink = intent.getStringExtra("URL_LINK") ?: return@run
            val browserFragment =
                supportFragmentManager.findFragmentById(R.id.browserFragment) ?: return@run
            if (browserFragment is BrowserFragment) {
                browserFragment.setUrl(urlLink)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}