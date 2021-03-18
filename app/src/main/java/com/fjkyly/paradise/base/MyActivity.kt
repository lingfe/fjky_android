package com.fjkyly.paradise.base

import androidx.appcompat.app.AppCompatActivity

abstract class MyActivity : AppCompatActivity() {

    open fun callAllInit() {
        initView()
        initData()
        initEvent()
        initPresenter()
    }

    open fun initView() {

    }

    open fun initData() {

    }

    open fun initEvent() {

    }

    open fun initPresenter() {

    }
}