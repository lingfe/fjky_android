package com.fjkyly.paradise.expand

import com.blankj.utilcode.util.GsonUtils

inline fun <reified T> fromJson(json: String): T = GsonUtils.fromJson(json, T::class.java)