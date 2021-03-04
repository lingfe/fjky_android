package com.fjkyly.paradise.ui.activity

import android.os.Bundle
import android.widget.SeekBar
import com.fjkyly.paradise.base.BaseActivity
import com.fjkyly.paradise.databinding.ActivityVolumeSettingBinding
import com.fjkyly.paradise.expand.simpleToast

class VolumeSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityVolumeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityVolumeSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initEvent() {
        mBinding.run {
            volumeBackIv.setOnClickListener {
                finish()
            }
            volumeChangeSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    volumeValueTv.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
            saveBtn.setOnClickListener {
                simpleToast("音量设置正在开发中...")
            }
        }
    }
}