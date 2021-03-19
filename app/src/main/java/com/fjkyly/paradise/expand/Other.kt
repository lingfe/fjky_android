package com.fjkyly.paradise.expand

import com.vondear.rxtool.RxRegTool

/**
 * 隐藏部分手机号码
 * @param phone
 * @return
 */
fun hidePhoneNum(phone: String?): String {
    var result = ""
    if (phone != null && "" != phone) {
        if (RxRegTool.isMobile(phone)) {
            result = phone.substring(0, 3) + "****" + phone.substring(7)
        }
    }
    return result
}