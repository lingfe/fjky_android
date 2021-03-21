package com.fjkyly.paradise.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fjkyly.paradise.base.MyActivity
import com.fjkyly.paradise.databinding.ActivityViewBigPhotoBinding

class ViewBitPhotoActivity : MyActivity() {

    private lateinit var mBinding: ActivityViewBigPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityViewBigPhotoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        callAllInit()
    }

    override fun initView() {
        var avatarUrl = intent.getStringExtra("avatarUrl")
        if (avatarUrl == null) {
            avatarUrl =
                "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2561659095,299912888&fm=26&gp=0.jpg"
        }
        Glide.with(this).load(avatarUrl).into(mBinding.photoIv)
    }

    override fun initEvent() {
        mBinding.run {
            photoContainer.setOnClickListener {
                finish()
            }
        }
    }

    companion object {
        fun startActivity(context: Context, avatarUrl: String?) {
            val intent = Intent(context, ViewBitPhotoActivity::class.java)
            intent.putExtra("avatarUrl", avatarUrl)
            context.startActivity(intent)
        }
    }
}