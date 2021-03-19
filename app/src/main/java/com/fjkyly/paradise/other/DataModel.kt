package com.fjkyly.paradise.other

object DataModel {

    private var mUrlLink: String? = null

    fun setUrlLink(url: String) {
        mUrlLink = url
    }

    fun getUrlLink(): String? = mUrlLink
}